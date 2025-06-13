import java.time.LocalDateTime;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

public class Statistics {
    private int totalTraffic = 0;
    private OffsetDateTime minTime;
    private OffsetDateTime maxTime;

    // Задание 1
    private Set<String> existingPages = new HashSet<>();//для существующий страниц
    private Map<String, Integer> osCounts = new HashMap<>(); //количество ОС

    private Set<String> missingPages = new HashSet<>();//несуществующие страницы
    private Map<String, Integer> browserCounts = new HashMap<>(); //количество браузеров


    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = OffsetDateTime.MAX;
        this.maxTime = OffsetDateTime.MIN;
    }

    //подсчёт среднего объёма трафика сайта за час
    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize(); //добавление объема данных от сервера

        //время в добавляемой записи из лога меньше minTime или больше maxTime
        if (entry.getTime().isBefore(minTime)) {
            minTime = entry.getTime();
        }
        if (entry.getTime().isAfter(maxTime)) {
            maxTime = entry.getTime();
        }

        int responseCode = entry.getResponseCode();
        String path = entry.getPath();

        //1
        // Добавить адрес существующей страницы (код 200)
        if (responseCode == 200) {
            existingPages.add(path);
        }

        // статистика ОС
        String os = entry.getUserAgent().getOs();
        osCounts.put(os, osCounts.getOrDefault(os, 0) + 1); //вставить либо добавить к значению ОС +1

        //2
        // Добавить адрес НЕсуществующей страницы (код 404)
        if (responseCode == 404) {
            missingPages.add(path);
        }

        // статистика браузеров
        String browser = entry.getUserAgent().getBrowser();
        browserCounts.put(browser, browserCounts.getOrDefault(browser, 0) + 1);//вставить либо добавить к значению браузера +1

    }

    //вычислить разницу между maxTime и minTime в часах
    public double getTrafficRate() {
        if (minTime == null || maxTime == null || minTime.equals(maxTime)) {
            return 0;
        }

        long hours = Duration.between(minTime, maxTime).toHours();
        return hours == 0 ? totalTraffic : ((double) totalTraffic / hours); //проверка часов на 0 и деление на разницу времени
    }

    public OffsetDateTime getMinTime() {
        return minTime;
    }

    public OffsetDateTime getMaxTime() {
        return maxTime;
    }

    //метод возвращающий список всех страниц
    public Set<String> getExistingPages() {
        return existingPages;
    }

    //метод возвращающий статистику ОС
    public Map<String, Double> getOperatingSystemStats() {
        return normalizeMap(osCounts);
    }

    //метод возвращающий список всех несуществующих страниц
    public Set<String> getMissingPages() {
        return missingPages;
    }

    //метод возвращающий статистику браузеров
    public Map<String, Double> getBrowserStats() {
        return normalizeMap(browserCounts);
    }

    private Map<String, Double> normalizeMap(Map<String, Integer> map) {
        Map<String, Double> result = new HashMap<>();
        //чтобы посчитать долю для каждой операционной системы (от 0 до 1) -
        //разделить количество конкретной операционной системы на общее количество для всех операционных систем.
        int total = map.values().stream().mapToInt(Integer::intValue).sum();
        if (total == 0) return result;

        map.forEach((key, value) -> result.put(key, (double)value / total));
        return result;
    }
}

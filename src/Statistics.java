import java.time.LocalDateTime;
import java.time.Duration;

public class Statistics {
    private int totalTraffic = 0;
    private LocalDateTime minTime = null;
    private LocalDateTime maxTime = null;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
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
    }

    //вычислить разницу между maxTime и minTime в часах
    public double getTrafficRate() {
        if (minTime == null || maxTime == null || minTime.equals(maxTime)) {
            return 0;
        }

        long hours = Duration.between(minTime, maxTime).toHours();
        return hours == 0 ? totalTraffic : ((double) totalTraffic / hours); //проверка часов на 0 и деление на разницу времени
    }
}

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime time;
    private final HttpMethod method;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final UserAgent userAgent;

    public enum HttpMethod {
        GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH, UNKNOWN
    }

    //getters for all fields
    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    //конструктор для парсинга строки по всем полям
    public LogEntry(String logLine) {
        try {
            String[] parts = logLine.split(" ");

            this.ipAddr = parts[0]; //первая часть - IP-адрес

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);//форматирование в дату-время
            this.time = LocalDateTime.parse(parts[3].substring(1), formatter);

            this.method = HttpMethod.valueOf(parts[5].substring(1));
            this.path = (parts[6] + " " + parts[7]).substring(0, (parts[6] + " " + parts[7]).length()-1);

            this.responseCode = Integer.parseInt(parts[8]);
            this.responseSize = Integer.parseInt(parts[9]);
            this.referer = parts[10].substring(1, parts[10].length()-1);

            StringBuilder result = new StringBuilder();
            int i = 11;
            for (; i < parts.length - 1; i++) {
                result.append(parts[i]).append(" ");
            }
            result.append(parts[i]);
            this.userAgent = new UserAgent(result.substring(1, result.length()-1));

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге строки: " + logLine, e);
            //System.out.println("Ошибка при парсинге строки: " + logLine);
            //e.printStackTrace(); //unreachable exception
        }
    }

    private HttpMethod parseMethod(String method) {
        try {
            return HttpMethod.valueOf(method);
        } catch (IllegalArgumentException e) {
            return HttpMethod.UNKNOWN;
        }
    }
}

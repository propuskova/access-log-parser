import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class LogEntry {
    private final String ipAddr;
    private final OffsetDateTime time;
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

    public OffsetDateTime getTime() {
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

            String dateTimeRaw = logLine.substring(logLine.indexOf("[") + 1, logLine.indexOf("]")); //подстрока из скобок []
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);//форматирование в дату-время
            this.time = OffsetDateTime.parse(dateTimeRaw, formatter);

            String methodAndPath = logLine.substring(logLine.indexOf("\"") + 1);//начиная с кавычки "
            String method = methodAndPath.split(" ")[0];
            this.method = parseMethod(method); //определение метода из enum
            this.path = methodAndPath.split(" ")[1]; //оставшаяся часть после пробела

            this.responseCode = Integer.parseInt(parts[parts.length - 4]); //4 с конца часть
            this.responseSize = Integer.parseInt(parts[parts.length - 3]); //3 с конца чатсь, тк не отделена кавычками в строке

            //определить положение кавычек для подстроки относительно строки
            int refererStart = logLine.indexOf('"', logLine.indexOf("\"", logLine.indexOf("\"") + 1) + 1);
            int refererEnd = logLine.indexOf('"', refererStart + 1);
            this.referer = logLine.substring(refererStart + 1, refererEnd);

            int userAgentStart = logLine.lastIndexOf('"', logLine.length() - 2);
            int userAgentEnd = logLine.length() - 1;
            String userAgent = logLine.substring(userAgentStart + 1, userAgentEnd);
            this.userAgent = new UserAgent(userAgent);

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

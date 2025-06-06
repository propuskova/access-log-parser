public class UserAgent {
    private final String os;
    private final String browser;

    public String getOs() {
        return os;
    }

    public String getBrowser() {
        return browser;
    }


    public UserAgent(String userAgentString) {
        String lower = userAgentString.toLowerCase();
        if (lower.contains("windows")) {
            os = "Windows";
        } else if (lower.contains("mac os")) {
            os = "macOS";
        } else if (lower.contains("linux")) {
            os = "Linux";
        } else {
            os = "Other";
        }

        if (lower.contains("chrome")) {
            browser = "Chrome";
        } else if (lower.contains("firefox")) {
            browser = "Firefox";
        } else if (lower.contains("edge")) {
            browser = "Edge";
        } else if (lower.contains("opera")) {
            browser = "Opera";
        } else if (lower.contains("safari")) {
            browser = "Safari";
        } else {
            browser = "Other";
        }
    }
}

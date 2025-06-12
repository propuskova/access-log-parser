import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Случайное число от 0 до 1: " + Math.random());
        System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());

        //выполнение задания из модуля Циклы и массивы
        int validateFileCount = 0; // Счетчик для корректных файлов

        //проверка количества и длин строк в файле
        int totalLines = 0;
        int maxLength = Integer.MIN_VALUE;
        int minLength = Integer.MAX_VALUE;

        //создание экземпляра статистики
        Statistics statistics = new Statistics();

        //проверка подсчета доли запросов от ботов
        int googleBotCount = 0;
        int yandexBotCount = 0;

        while (true) {
            System.out.print("Введите путь к файлу: ");
            String path = new Scanner(System.in).nextLine(); // запрос пути к файлу

            File file = new File(path); // создание объекта File

            boolean fileExists = file.exists(); // проверка на существование файла
            boolean isDirectory = file.isDirectory(); // проверка, является ли путь директорией

            if (!fileExists || isDirectory) {
                System.out.println("Указанный файл не существует или это путь к папке, а не к файлу.");
                continue; // продолжать запрос, если файл не существует или это директория
            }
            else {                 // Если файл существует и это действительно файл
                validateFileCount++;
                System.out.println("Путь указан верно.");
                System.out.println("Это файл номер " + validateFileCount);
            }

            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024) {
                        throw new RuntimeException("В файле есть строка длиной более 1024 символов!");
                    }

                    //подсчет количества строк и мин-макс длины
                    totalLines++;
                    maxLength = Math.max(maxLength, length);
                    minLength = Math.min(minLength, length);

                    // извлечение User-Agent. Поиск начала и конца User-Agent
                    // пример - "Mozilla/5.0 (compatible; YandexBot/3.0; +http://yandex.com/bots)"
                    // отделяем по кавычкам ""
                    int userAgentStart = line.indexOf('"', line.indexOf("\"", line.indexOf("\"") + 1) + 1);
                    int userAgentEnd = line.lastIndexOf('"');

                    if (userAgentStart != -1 && userAgentEnd != -1 && userAgentStart < userAgentEnd) {
                        String userAgent = line.substring(userAgentStart + 1, userAgentEnd); // выделитб подстроку из "..."
                        int firstBracketOpen = userAgent.indexOf('('); //найти часть сообщения в скобках (...)
                        int firstBracketClose = userAgent.indexOf(')');
                        if (firstBracketOpen != -1 && firstBracketClose != -1 && firstBracketOpen < firstBracketClose) {
                            String firstBrackets = userAgent.substring(firstBracketOpen + 1, firstBracketClose); // выделить подстроку из (...)
                            String[] parts = firstBrackets.split(";"); //раздедить строку на части по знаку ;
                            if (parts.length >= 2) {
                                String fragment = parts[1].trim(); // взять вторую часть
                                if (fragment.contains("/")) {
                                    String botName = fragment.substring(0, fragment.indexOf('/')).trim(); //выделить подстроку без пробелов до знака /
                                    if (botName.equals("Googlebot")) {
                                        googleBotCount++; //сравнение и подсчет GoogleBot
                                    } else if (botName.equals("YandexBot")) {
                                        yandexBotCount++; //сравнение и подсчет YandexBot
                                    }
                                }
                            }
                        }
                    }

                    //применение класса LogEntry для парсинга строки
                    try {
                        LogEntry logEntry = new LogEntry(line);
                        statistics.addEntry(logEntry);     //подсчёт среднего объёма трафика сайта за час
                    } catch (Exception e) {
                        System.out.println("Ошибка парсинга строки: " + line);
                        e.printStackTrace();
                    }
                }
                reader.close();
                System.out.println("Общее количество строк в файле: " + totalLines);
                if (totalLines > 0) {
                    double googleShare = (googleBotCount * 100.0) / totalLines;
                    double yandexShare = (yandexBotCount * 100.0) / totalLines;
                    System.out.printf("Доля запросов от Googlebot: %.2f%%%n", googleShare);
                    System.out.printf("Доля запросов от YandexBot: %.2f%%%n", yandexShare);

                    double trafficRate = statistics.getTrafficRate();
                    System.out.println("Средний объем трафика в час: " + trafficRate);
                    System.out.println("Мин время запроса: " + statistics.getMinTime());
                    System.out.println("Макс время запроса: " + statistics.getMaxTime());


                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}




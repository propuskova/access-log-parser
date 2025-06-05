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
                BufferedReader reader =
                        new BufferedReader(fileReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length > 1024) {
                        throw new RuntimeException("В файле есть строка длиной более 1024 символов!");
                    }
                    totalLines++;
                    maxLength = Math.max(maxLength, length);
                    minLength = Math.min(minLength, length);
                }
                reader.close();
                System.out.println("Общее количество строк в файле: " + totalLines);
                System.out.println("Длина самой длинной строки: " + maxLength);
                System.out.println("Длина самой короткой строки: " + minLength);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}




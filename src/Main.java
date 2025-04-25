import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число: ");
        int firstNumber = new Scanner(System.in).nextInt();

        System.out.println("Введите второе число: ");
        int secondNumber = new Scanner(System.in).nextInt();
        //сложение
        int sum = firstNumber + secondNumber;
        System.out.println("Сумма двух чисел = " + sum);
        //вычитание
        int sub = firstNumber - secondNumber;
        System.out.println("Разность двух чисел = " + sub);
        //умножение
        int multi = firstNumber * secondNumber;
        System.out.println("Произведение двух чисел = " + multi);
        //деление - частное
        double div = (double) firstNumber/secondNumber;
        if (secondNumber != 0) {
            System.out.println("Деление двух чисел = " + div);
        } else System.out.println("Ошибка! Делить на 0 нельзя.");

    }
}
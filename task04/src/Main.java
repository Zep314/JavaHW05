import java.util.Arrays;

/*
Задание 4.
На шахматной доске расставить 8 ферзей так, чтобы они не били друг друга.
 */
public class Main {
    static int[] chessboard = {0,0,0,0,0,0,0,0}; // Позиции в строках, на которых стоят ферзи
    static int index = 0; // указатель на текущую строку доски

    public static void main(String[] args) {
        chessboard[0] = (int) Math.round(Math.random() * 7); // Ставим ферзя в первой строке случайно
        index = 1;  // Работаем, начиная со 2-й строки
        while (index < 8) {
            if (checking()){
                index++;
            }
            else {
                chessboard[index]++;
            }
        }
        System.out.println(Arrays.toString(chessboard));
        MyPrettyPrint();
    }

    public static boolean checking() {
        int i;

        if (index == 0) {  // Если работаем только с 1-й строкой, то все ОК
            return true;
        }

        if (chessboard[index]>7){  // Вышли за пределы доски в текущей строке
            chessboard[index] = 0; // обнуляем текущую строку
            index--;               // Возвращаемся в предыдущую строку
            return false;
        }

        for (i=0;i<index;i++){  // Горизонтали не проверяем, т.к. мы сами работаем только
                                // с одной фигурой в горизонтали
            if ((chessboard[index] == chessboard[i]) |  // Проверяем вертикали
                    ((Math.abs(chessboard[index] - chessboard[i])) == (index-i))){  // Проверяем диагонали
                return false;
            }
        }
        return true;  // Все проверки прошли, значит - все ОК
    }

    public static void MyPrettyPrint() {  // Красиво печатаем шахматную доску
        final String hLine = "+---+---+---+---+---+---+---+---+";
        for(int i=0; i<8; i++) {
            System.out.println(hLine);
            for(int j=0; j<8; j++) {
                if (chessboard[i] == j) {
                    System.out.print("| * ");
                }
                else {
                    System.out.print("|   ");
                }
            }
            System.out.println("|");
        }
        System.out.println(hLine);
    }
}

/* Вывод программы:
[6, 0, 2, 7, 5, 3, 1, 4]
+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   | * |   |
+---+---+---+---+---+---+---+---+
| * |   |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+
|   |   | * |   |   |   |   |   |
+---+---+---+---+---+---+---+---+
|   |   |   |   |   |   |   | * |
+---+---+---+---+---+---+---+---+
|   |   |   |   |   | * |   |   |
+---+---+---+---+---+---+---+---+
|   |   |   | * |   |   |   |   |
+---+---+---+---+---+---+---+---+
|   | * |   |   |   |   |   |   |
+---+---+---+---+---+---+---+---+
|   |   |   |   | * |   |   |   |
+---+---+---+---+---+---+---+---+
 */
/*
Задание 3.
Реализовать алгоритм пирамидальной сортировки (HeapSort).
 */
public class Main {
    // Константы
    final static int MAX_ARRAY_LENGTH = 30;
    final static int MAX_ARRAY_NUMBER = 100;
    public static void main(String[] args) {
        System.out.println("Начинаем работу, инициализируем массив случайной величины");
        int[] testArray = new int[(int) (Math.random() * MAX_ARRAY_LENGTH)];
        System.out.println("Заполняем массив случайными числами");
        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = (int) (Math.random() * 2 * MAX_ARRAY_NUMBER) - MAX_ARRAY_NUMBER;
        }
        System.out.println("Исходный массив:");
        System.out.println(myGetArray(testArray));

        heapSort(testArray);// сортировка пузырьком

        System.out.println("Массив после сортировки");
        System.out.println(myGetArray(testArray));
    }
    public static String myGetArray(int[] arrayIn) {
        // возвращаем красивую строку для печати
        StringBuilder ret = new StringBuilder("");
        ret.append("[ ");
        for (int i=0; i<arrayIn.length; i++) {
            if (i != 0) {
                ret.append(", ");
            }
            ret.append(arrayIn[i]);
        }
        ret.append(" ]");
        return ret.toString();
    }
    public static void heapSort(int[] arrayIn) {  // пирамидальная сортировка

        int n = arrayIn.length;

        // Построение кучи (перегруппируем массив)
        for (int i = n / 2 - 1; i >= 0; i--)
            makeHeap(arrayIn, n, i);

        // Один за другим извлекаем элементы из кучи
        for (int i=n-1; i>=0; i--)
        {
            // Перемещаем текущий корень в конец
            int swap = arrayIn[0];
            arrayIn[0] = arrayIn[i];
            arrayIn[i] = swap;

            // Вызываем процедуру heapify на уменьшенной куче
            makeHeap(arrayIn, i, 0);
        }
    }

    public static void makeHeap(int[] arrayIn, int n, int i)
    // Процедура для преобразования в двоичную кучу поддерева с корневым узлом i, что является
    // индексом в arrayIn[]. n - размер кучи
    {
        int largest = i; // Инициализируем наибольший элемент как корень
        int l = 2*i + 1; // левый = 2*i + 1
        int r = 2*i + 2; // правый = 2*i + 2

        // Если левый дочерний элемент больше корня
        if (l < n && arrayIn[l] > arrayIn[largest])
            largest = l;

        // Если правый дочерний элемент больше, чем самый большой элемент на данный момент
        if (r < n && arrayIn[r] > arrayIn[largest])
            largest = r;
        // Если самый большой элемент не корень
        if (largest != i)
        {
            int swap = arrayIn[i];
            arrayIn[i] = arrayIn[largest];
            arrayIn[largest] = swap;

            // Рекурсивно преобразуем в двоичную кучу затронутое поддерево
            makeHeap(arrayIn, n, largest);
        }
    }
}

/* Вывод программы:
Начинаем работу, инициализируем массив случайной величины
Заполняем массив случайными числами
Исходный массив:
[ 26, 63, 47, -99, -83, -80, -77, 30, 52, 72, -17, -95, 64, -78, -99, -89, 89, 83, -41, 15, 93, 69, -89, 25, 12, -36, -50 ]
Массив после сортировки
[ -99, -99, -95, -89, -89, -83, -80, -78, -77, -50, -41, -36, -17, 12, 15, 25, 26, 30, 47, 52, 63, 64, 69, 72, 83, 89, 93 ]
 */
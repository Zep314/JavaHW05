/*
Задание 2.
Пусть дан список сотрудников:

Иван Иванов
Светлана Петрова
Кристина Белова
Анна Мусина
Анна Крутова
Иван Юрин
Петр Лыков
Павел Чернов
Петр Чернышов
Мария Федорова
Марина Светлова
Мария Савина
Мария Рыкова
Марина Лугова
Анна Владимирова
Иван Мечников
Петр Петин
Иван Ежов

Написать программу, которая найдет и выведет повторяющиеся имена с количеством повторений.
Отсортировать по убыванию популярности.
 */
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final boolean ASC = true;
    private static final boolean DESC = false;
    public static void main(String[] args) {
        final String[] arrayIn = new String[] {
                "Иван Иванов",
                "Светлана Петрова",
                "Кристина Белова",
                "Анна Мусина",
                "Анна Крутова",
                "Иван Юрин",
                "Петр Лыков",
                "Павел Чернов",
                "Петр Чернышов",
                "Мария Федорова",
                "Марина Светлова",
                "Мария Савина",
                "Мария Рыкова",
                "Марина Лугова",
                "Анна Владимирова",
                "Иван Мечников",
                "Петр Петин",
                "Иван Ежов",
        };
        Map<String, Integer> db = new HashMap<String, Integer>(); // Создаем коллекцию
        for (String s: arrayIn) {
            String name = s.split(" ",2)[0];  // Выделяем только имя
            if (db.containsKey(name)) {
                db.put(name,db.get(name) + 1); // Если имя уже есть в коллекции
            }
            else {
                db.put(name,1);  // Если такого имени еще в коллекции нет
            }
        }
        System.out.println("Несортированная коллекция");
        System.out.println(db);
        System.out.println("Отсортированная коллекция");
        Map<String, Integer> sortedDB = sortByValue(db,DESC);
        System.out.println(sortedDB);
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> unsortMap, final boolean order)
    {  // Сортировка коллекции (можно в обе стороны сортировать)
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Сортируем коллекцию, опираясь на значения ключей
        list.sort((element1, element2) -> order ?
                element1.getValue().compareTo(element2.getValue()) == 0  // Сортировка ASC
                ? element1.getKey().compareTo(element2.getKey())
                : element1.getValue().compareTo(element2.getValue()) :
                element2.getValue().compareTo(element1.getValue()) == 0  // Сортировка DESC
                ? element2.getKey().compareTo(element1.getKey())
                : element2.getValue().compareTo(element1.getValue()));

        return list.stream().collect(Collectors.toMap(Map.Entry::getKey,
                Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }
}
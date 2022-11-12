/*
Задание 1.
Реализуйте структуру телефонной книги с помощью HashMap, учитывая,
что 1 человек может иметь несколько телефонов.
 */

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {  // Почти полноценный телефонный справочник
    final static String DB_NAME = "db.json";
    static Map<String, ArrayList> db = new HashMap<String, ArrayList>(); // Наша база данных
    static String current = "";  // Ключ текущей записи
    public static void main(String[] args) throws IOException, ParseException {
        Scanner scan = new Scanner(System.in);
        String inputLine = "";
        boolean ex = false;
        Info();       // Информация о программе
        LoadFromFile(DB_NAME);  // Загрузка базы
        while (!ex) {  // главный цикл программы
            System.out.print(">>> ");
            inputLine = scan.nextLine();
            if (inputLine.length()>0) {
                switch (inputLine.split(" ",2)[0]) {  // в первой части введенной строки
                                                                 // ждем команду
                    case "/quit" -> ex = true;
                    case "/info" -> Info();
                    case "/help" -> Help();
                    case "/addRecord" -> AddRecord(inputLine.split(" ",2)[1]);
                    case "/addPhone" -> AddPhone(inputLine.split(" ",2)[1]);
                    case "/printAll" -> PrintAll();
                    case "/printCurrent" -> PrintCurrent(current);
                    case "/setRecord" -> SetRecord(inputLine.split(" ",2)[1]);
                    case "/save" -> SaveToFile(inputLine.split(" ",2)[1]);
                    case "/load" -> LoadFromFile(inputLine.split(" ",2)[1]);
                    default -> System.out.println("Неверная команда. Для помощи наберите \"/help\"");
                }
            }
        }
        System.out.println("Работа завершена.");
        SaveToFile(DB_NAME);  // Запись всей базы
    }

    public static void Info() {  // Информация о программе
        System.out.println("Программа - телефонный справочник");
        System.out.println("Для помощи наберите \"/help\"");
    }
    public static void Help() {  // Вывод помощи
        System.out.println("Программа - поддерживает следующие команды:");
        System.out.println("/help - вывод помощи");
        System.out.println("/info - вывод информации о программе");
        System.out.println("/printAll - печать всей базы целиком");
        System.out.println("/printCurrent - печать текущей записи базы данных");
        System.out.println("/addRecord <name> - добавление записи с именем <name>");
        System.out.println("/addPhone <phoneNumber> - добавление номера телефона <phoneNumber> в текущую запись");
        System.out.println("/setRecord <name> - установка записи <name> в качестве текущей");
        System.out.println("/save <file> - сохранение базы в файл <file> в формате JSON");
        System.out.println("/load <file> - чтение из файла <file> в формате JSON базы данных");
    }

    public static void AddRecord(String inputString) {  // Добавление новой записи в БД
        if (inputString.length() > 0) {
            if (!db.containsKey(inputString)) {
                db.put(inputString, new ArrayList<String>());
                System.out.println("Запись добавлена.");
            } else {
                System.out.println("Запись с таким именем уже есть!");
            }
            current = inputString;  // Установка указателя на новую запись
        }
        else {
            System.out.println("Укажите имя абонента!\n Запись не добавлена!");
        }
    }

    public static void AddPhone(String inputString) {  // Добавление нового телефона в текущую запись
        if (current.length() == 0) {
            System.out.println("База данных пуста! Не к чему добавлять телефон!");
            System.out.println("Сначала добавьте запись в базу данных!");
        }
        else {
            if (inputString.length() > 0) {
                ArrayList<String> phones = db.get(current);
                phones.add(inputString);
                System.out.println("Номер телефона добавлен.");
            }
            else {
                System.out.println("Укажите номер телефона!\n Номер телефона не добавлен!");
            }
        }
    }

    public static void PrintCurrent(String record) {  // Вывод на печать текущей записи
        if (!db.containsKey(record)) {
            System.out.println("Записи с таким именем нет!");
        }
        else {
            ArrayList<String> phones = db.get(record);
            System.out.printf("Имя: %s%n",record);  // Наименование записи
            if (phones.size() == 0) {
                System.out.printf("Список телефонов у %s пуст.%n", record);
            }
            else {
                for(String s: phones) {  // Список всех телефонов текущей записи
                    System.out.printf("\t %s%n",s);
                }
            }
        }
    }
    public static void PrintAll() {  // Вывод на печать всей базы
        for(String s: db.keySet()) {
            PrintCurrent(s);
            System.out.println();  // Разделитель между записями
        }
    }

    public static void SetRecord(String record) {  // Установка текущей записи (по ее имени)
        if (db.containsKey(record)) {
            current = record;
            System.out.printf("Текущая запись %s установлена!%n", record);
        }
        else {
            System.out.printf("Записи с именем %s не существует!%n Текущей записью остается %s%n", record, current);
        }
    }
    public static void SaveToFile(String filename) throws IOException {  // формирование JSON и запись его в файл
        JSONObject jsonObj = new JSONObject();
        for(String s: db.keySet()) {   // Проходим по всей базе и формируем JSON объект
            JSONArray jsonArr = new JSONArray();
            ArrayList<String> phones = db.get(s);
            jsonArr.addAll(phones);  // пишем все телефоны разом
            jsonObj.put(s,jsonArr);
        }
        Files.write(Paths.get(filename), jsonObj.toJSONString().getBytes());  // пишем JSON в файл
        System.out.printf("Данные записаны в файл %s%n",filename);
    }

    public static void LoadFromFile(String filename) throws IOException, ParseException {
    // Чтение базы из файла формата JSON
        if (new File(filename).exists()) {
            ClearDB(); // Очистим всю базу
            FileReader reader = new FileReader(filename);               // Читаем файл
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(reader); // И парсим его в JSON объект
            for(String s: (Set<String>)jsonObj.keySet()) {              // Формируем нашу базу данных
                ArrayList<String> phones = new ArrayList<String>();     // из JSON объекта
                db.put(s, phones);
                if (current.length() == 0 ) {  // указатель текущей записи устанавливаем
                    current = s;               // на первую считанную запись
                }
                phones.addAll((ArrayList<String>) jsonObj.get(s));  // устанавливаем все телефоны разом
            }
            System.out.printf("Данные прочитаны из файла %s%n", filename);
        }
        else {
            System.out.printf("Файл %s не найден! Загружена пустая база данных!%n", filename);
        }
    }

    public static void ClearDB() {  // очищаем всю базу
        for(String s: db.keySet()) {
            ArrayList phones = db.get(s);
            phones.clear();
        }
        db.clear();
        current = "";
    }
}


/* Вывод программы:
Программа - телефонный справочник
Для помощи наберите "/help"
Данные прочитаны из файла db.json
>>> /help
Программа - поддерживает следующие команды:
/help - вывод помощи
/info - вывод информации о программе
/printAll - печать всей базы целиком
/printCurrent - печать текущей записи базы данных
/addRecord <name> - добавление записи с именем <name>
/addPhone <phoneNumber> - добавление номера телефона <phoneNumber> в текущую запись
/setRecord <name> - установка записи <name> в качестве текущей
/save <file> - сохранение базы в файл <file> в формате JSON
/load <file> - чтение из файла <file> в формате JSON базы данных
>>> /printCurrent
Имя: Ivan
	 123-123
	 321-321
>>> /printAll
Имя: Ivan
	 123-123
	 321-321

Имя: Marina
	 111-222

>>> /addRecord Egor
Запись добавлена.
>>> /addPhone 34-34-34
Номер телефона добавлен.
>>> /addPhone 56-56-56
Номер телефона добавлен.
>>> /addPhone 78-78-78
Номер телефона добавлен.
>>> /addRecord Olga
Запись добавлена.
>>> /save db.json
Данные записаны в файл db.json
>>> /save db_save.json
Данные записаны в файл db_save.json
>>> /addRecord Qwerty
Запись добавлена.
>>> /load db_save.json
Данные прочитаны из файла db_save.json
>>> /printAll
Имя: Olga
Список телефонов у Olga пуст.

Имя: Egor
	 34-34-34
	 56-56-56
	 78-78-78

Имя: Ivan
	 123-123
	 321-321

Имя: Marina
	 111-222

>>> /setRecord Egor
Текущая запись Egor установлена!
>>> /printCurrent
Имя: Egor
	 34-34-34
	 56-56-56
	 78-78-78
>>> /quit
Работа завершена.
Данные записаны в файл db.json
 */
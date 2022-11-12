/*
Задание 1.
Реализуйте структуру телефонной книги с помощью HashMap, учитывая,
что 1 человек может иметь несколько телефонов.
 */

import java.io.FileNotFoundException;
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

import javax.sql.RowSetReader;

public class Main {
    final static String DB_NAME = "db.json";
    static Map<String, ArrayList> db = new HashMap<String, ArrayList>();
    static String current = "";
    public static void main(String[] args) throws IOException, ParseException {
        Scanner scan = new Scanner(System.in);
        String inputLine = "";
        boolean ex = false;
        Info();
        LoadFromFile(DB_NAME);
        while (!ex) {
            System.out.print(">>> ");
            inputLine = scan.nextLine();
            if (inputLine.length()>0) {
                switch (inputLine.split(" ",2)[0]) {
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
        SaveToFile(DB_NAME);
    }

    public static void Info() {
        System.out.println("Программа - телефонный справочник");
        System.out.println("Для помощи наберите \"/help\"");
    }
    public static void Help() {
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

    public static void AddRecord(String inputString) {
        if (inputString.length() > 0) {
            if (!db.containsKey(inputString)) {
                db.put(inputString, new ArrayList<String>());
                System.out.println("Запись добавлена.");
            } else {
                System.out.println("Запись с таким именем уже есть!");
            }
            current = inputString;
        }
        else {
            System.out.println("Укажите имя абонента!\n Запись не добавлена!");
        }
    }

    public static void AddPhone(String inputString) {
        if (current.length() == 0) {
            System.out.println("База данных пуста! Нек чему добавлять телефон!");
            System.out.println("Сначала добавьте запись в базу данных!");
        }
        else {
            if (inputString.length() > 0) {
                ArrayList phones = db.get(current);
                phones.add(inputString);
                System.out.println("Номер телефона добавлен.");
            }
            else {
                System.out.println("Укажите номер телефона!\n Номер телефона не добавлен!");
            }
        }
    }

    public static void PrintCurrent(String record) {
        if (!db.containsKey(record)) {
            System.out.println("Записи с таким именем нет!");
        }
        else {
            ArrayList<String> phones = db.get(record);
            System.out.printf("Имя: %s%n",record);
            if (phones.size() == 0) {
                System.out.printf("Список телефонов у %s пуст.%n", record);
            }
            else {
                for(String s: phones) {
                    System.out.printf("\t %s%n",s);
                }
            }
        }
    }
    public static void PrintAll() {
        for(String s: db.keySet()) {
            PrintCurrent(s);
            System.out.println();
        }
    }

    public static void SetRecord(String record) {
        if (db.containsKey(record)) {
            current = record;
            System.out.printf("Текущая запись %s установлена!%n", record);
        }
        else {
            System.out.printf("Записи с именем %s не существует!%n Текущей записью остается %s%n", record, current);
        }
    }
    public static void SaveToFile(String filename) throws IOException {
        JSONObject jsonObj = new JSONObject();
        for(String s: db.keySet()) {
            JSONArray jsonArr = new JSONArray();
            ArrayList<String> phones = db.get(s);
            for (String p: phones) {
                jsonArr.add(p);
            }
            jsonObj.put(s,jsonArr);
        }
        Files.write(Paths.get(filename), jsonObj.toJSONString().getBytes());
        System.out.printf("Данные записаны в файл %s%n",filename);
    }

    public static void LoadFromFile(String filename) throws IOException, ParseException {
        if (new File(filename).exists()) {
            FileReader reader = new FileReader(filename);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(reader);
            for(String s: (ArrayList<String>)jsonObj.keySet()) {
                ArrayList<String> phones = new ArrayList<String>();
                db.put(s, phones);
                if (current.length() == 0 ) {
                    current = s;
                }
                for(String p: (ArrayList<String>)jsonObj.get(s)) {
                    phones.add(p);
                }
            }
            System.out.printf("Данные прочитаны из файла %s%n", filename);
        }
        else {
            System.out.printf("Файл %s не найден! Загружена пустая база данных!%n", filename);
        }
    }
}

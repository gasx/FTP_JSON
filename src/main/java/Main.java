import methods.FTP;
import methods.UserMethods;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String host = "";
        String login = "";
        String password = "";

        Scanner scanner = new Scanner(System.in);
        FTP ftp = new FTP(host, login, password, "users.json");

        System.out.println("Доступные пункты: \n1. Получить списка студентов по имени \n" +
                "2. Получение информации о студенте по ID \n3. Добавление студента \n4. Удалить студента по ID " +
                "\n5. Завершение работы");

        for(;;) {
            System.out.print("Выберите нужный вам пункт: ");
            int selected = scanner.nextInt();

            JSONObject jsonObject = ftp.getJSON();
            switch(selected) {
                case 1:
                    JSONArray jsonArray = (JSONArray) jsonObject.get("students");

                    if(jsonArray.size() < 1) {
                        System.out.println("\n\nСтудентов нет в базе.\n");
                    } else {
                        System.out.println("\n\nСписок пользователей из базы:");
                        for(Object item : jsonArray) {
                            JSONObject obj = (JSONObject) item;
                            System.out.println("ID: " + obj.get("id") + "\nИмя: " + obj.get("name") + "\n");
                        }
                    }
                    break;
                case 2:
                    System.out.print("Введите ID пользователя: ");
                    int needId = scanner.nextInt();

                    JSONObject user = UserMethods.getById(needId, jsonObject);
                    if(user == null) {
                        System.out.println("Такого пользователя нет в базе данных!");
                    } else {
                        System.out.println("\n\nПользователь найден в базе данных.\nID: " + user.get("id") + "\nИмя: " + user.get("name") + "\n");
                    }
                    break;
                case 3:
                    System.out.print("Введите имя будущего пользователя: ");
                    String name = scanner.next();

                    if(name.equals("")) {
                        System.out.println("\nВведено неверное имя!");
                        break;
                    }

                    if(UserMethods.addUser(name, jsonObject)) {
                        System.out.println("\nПользователь был добавлен в базу данных.");
                        if(!ftp.Upload(jsonObject)) {
                            System.out.println("\n\nВнимание! Не удалось загрузить измененный файл на FTP сервер!\n");
                        }
                    } else {
                        System.out.println("\nНе удалось добавить пользователя в базу данных.");
                    }
                    break;
                case 4:
                    System.out.print("Введите ID пользователя: ");
                    int userId = scanner.nextInt();

                    if(UserMethods.deleteUser(userId, jsonObject)) {
                        System.out.println("\nПользователь успешно удален из базы данных.");
                        if(!ftp.Upload(jsonObject)) {
                            System.out.println("\n\nВнимание! Не удалось загрузить измененный файл на FTP сервер!\n");
                        }
                    } else {
                        System.out.println("\nПользователь с таким ID не был найден в базе данных.");
                    }
                    break;
                default:
                    System.out.println("До свидания!");

                    scanner.close();
                    System.exit(0);
            }
        }
    }
}


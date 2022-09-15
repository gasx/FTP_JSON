package interfaces;

import org.json.simple.JSONObject;

public interface Methods {

    public static boolean addUser(String name, JSONObject jsonObject) {
        return false;
    }
    public static JSONObject getById(int id, JSONObject jsonObject) {
        return null;
    }
    public static JSONObject getByName(String name, JSONObject jsonObject) {
        return null;
    }
    public static boolean deleteUser(int id, JSONObject jsonObject) {
        return false;
    }

}

package methods;

import interfaces.Methods;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class UserMethods implements Methods {

    /**
     * Class for working with JSONObject.
     */

    // Function for add user in JSONArray "students"
    public static boolean addUser(String name, JSONObject jsonObject) {
        JSONArray students = (JSONArray) jsonObject.get("students");

        int nextId = 1;
        for (Object item : students) {
            JSONObject obj = (JSONObject) item;

            int tempNextId = Integer.parseInt(obj.get("id").toString()) + 1;
            if(tempNextId > nextId) {
                nextId = tempNextId;
            }
        }

        JSONObject student = new JSONObject();
        student.put("id", nextId);
        student.put("name", name);

        students.add(student);
        return true;
    }

    // Function for find user in JSONArray "students" by id
    public static JSONObject getById(int id, JSONObject jsonObject) {
        JSONArray students = (JSONArray) jsonObject.get("students");

        for (Object item : students) {
            JSONObject obj = (JSONObject) item;

            int objId = Integer.parseInt(obj.get("id").toString());
            if(objId == id) {
                return obj;
            }
        }

        return null;
    }

    // Function for find user in JSONArray "students" by name
    public static JSONObject getByName(String name, JSONObject jsonObject) {
        JSONArray students = (JSONArray) jsonObject.get("students");

        for (Object item : students) {
            JSONObject obj = (JSONObject) item;
            if(obj.get("name") == name) {
                return obj;
            }
        }

        return null;
    }

    // Function for delete user in JSONArray "students"
    public static boolean deleteUser(int id, JSONObject jsonObject) {
        JSONArray students = (JSONArray) jsonObject.get("students");
        for(int i = 0; i < students.size(); i++) {
            JSONObject obj = (JSONObject) students.get(i);

            int objId = Integer.parseInt(obj.get("id").toString());
            if(objId == id) {
                students.remove(i);
                return true;
            }
        }

        return false;
    }
}

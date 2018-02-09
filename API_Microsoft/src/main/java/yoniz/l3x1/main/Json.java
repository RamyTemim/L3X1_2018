package yoniz.l3x1.main;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Json {

    public static String JsonDisplay(String jsonString)
    {
        // Format and display the JSON response.
        String response = "REST Response:\n";

        if (jsonString.charAt(0) == '[') {
            JSONArray jsonArray = new JSONArray(jsonString);
            return jsonArray.toString(2);
        }
        else if (jsonString.charAt(0) == '{') {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.toString(2);
        } else {
            return jsonString;
        }
    }
}

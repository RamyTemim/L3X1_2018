package yoniz.l3x1.main;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Json {

    public static String httpToString(HttpEntity entity)
    {
        String jsonString="";
        if (entity!=null)
        {
            try {
                jsonString=EntityUtils.toString(entity).trim();
                jsonString = stringToJson(jsonString);
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        return jsonString;
    }

    private static String stringToJson (String jsonString)
    {
        // Format and display the JSON response.
        String response = "REST Response:\n";

        if (jsonString.charAt(0) == '[') {
            JSONArray jsonArray = new JSONArray(jsonString);
            return jsonArray.toString(1);
        }
        else if (jsonString.charAt(0) == '{') {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.toString(1);
        }
        else {
            return jsonString;
        }
    }

    public static void jsonToFile (String json, String path)
    {
        String name = pathToNameOfFile(path);
        try {
            PrintWriter pw = new PrintWriter(name);
            pw.println(json);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String pathToNameOfFile(String path)
    {
        return path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
    }
}

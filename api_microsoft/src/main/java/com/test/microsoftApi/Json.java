package com.test.microsoftApi;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Json {

    public static void JsonDisplay(String jsonString)
    {
        // Format and display the JSON response.
        System.out.println("REST Response:\n");

        if (jsonString.charAt(0) == '[') {
            JSONArray jsonArray = new JSONArray(jsonString);
            System.out.println(jsonArray.toString(2));
        }
        else if (jsonString.charAt(0) == '{') {
            JSONObject jsonObject = new JSONObject(jsonString);
            System.out.println(jsonObject.toString(2));
        } else {
            System.out.println(jsonString);
        }
    }
}

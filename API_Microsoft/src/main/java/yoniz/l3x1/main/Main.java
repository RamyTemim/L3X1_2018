package yoniz.l3x1.main;// This sample uses the Apache HTTP client library(org.apache.httpcomponents:httpclient:4.2.4)
// and the org.json library (org.json:json:20170516).

import java.io.File;
import java.io.IOException;
import java.net.URI;

import com.cedarsoftware.util.io.JsonWriter;
import org.apache.http.HttpEntity;

import org.apache.http.util.EntityUtils;


public class Main {

    public static void main(String[] args) {
        String path = "/Users/yonizana/L3X1_2018/api_microsoft/src/main/resources/yoni1.jpg";
        HttpEntity entity = RequeteVersAPI.requete(path);
        String jsonString = "";
        try {
            jsonString=EntityUtils.toString(entity).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = JsonWriter.formatJson(jsonString);
        System.out.println(json);


    }
}
package yoniz.l3x1.main;// This sample uses the Apache HTTP client library(org.apache.httpcomponents:httpclient:4.2.4)
// and the org.json library (org.json:json:20170516).

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import com.cedarsoftware.util.io.JsonWriter;
import org.apache.http.HttpEntity;

import org.apache.http.util.EntityUtils;


public class Main {

    public static void main(String[] args) {
        String detectOnImage = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

        String path = "src/main/resources/yoni2.jpg";
        HttpEntity entity = RequeteVersAPI.requete(path, detectOnImage);
        String jsonString = Json.httpToString(entity);

        //Création d'un fichier à partir du nom de fichier
        Json.jsonToFile(jsonString, path.substring(path.lastIndexOf("/")+1, path.lastIndexOf(".")));






    }
}
package yoniz.l3x1.main;// This sample uses the Apache HTTP client library(org.apache.httpcomponents:httpclient:4.2.4)
// and the org.json library (org.json:json:20170516).

import org.apache.http.HttpEntity;

public class Main {

    public static void main(String[] args) {
        String detectOnImage = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

        String path = "src/main/resources/yoni1.jpg";
        HttpEntity entity = RequeteVersAPI.requete(path, detectOnImage);
        String jsonString = Json.httpToString(entity);

        //Création d'un fichier à partir du nom de fichier
        Json.jsonToFile(jsonString, path);
    }
}
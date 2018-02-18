package yoniz.l3x1.apiFace;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.util.JsonUtil;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        //String detectOnImage = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";
        //FaceList.deleteFaceList("l");
        String pathHocine = "src/main/resources/hocine.jpg";
        String pathJordan = "src/main/resources/jordan.jpg";
        String pathYoni1 = "src/main/resources/yoni1.jpg";
        String pathYoni2 = "src/main/resources/yoni2.jpg";
        String pathYoniModele = "src/main/resources/yoni_modele.jpg";

        FaceList.create("Test de reconnaissance", "l", "yoni");
        System.out.println("Ajout des photos dans la liste l : ");
        FaceList.addFace(pathHocine,"l","Hocine");
        FaceList.addFace(pathJordan,"l","Jordan");
        FaceList.addFace(pathYoni1,"l","Yoni1");
        FaceList.addFace(pathYoni2,"l","Yoni2");

        HttpEntity entity = DetectFace.requete(pathYoniModele, "");
        JSONObject jsonObject = JsonUtil.httpToJsonObject(entity);
        String faceIdModele = jsonObject.get("faceId").toString();

        //Voir comment maxNbPersonne fonctionne...
        JSONObject jsonResultat = DetectFace.findSimilar("l", faceIdModele, "1");

        System.out.println("\nRetour de detectFace :");
        if(jsonResultat !=null)
            System.out.println(jsonResultat);
        else
            System.out.println("Aucune photo ressemblante trouvé");

        System.out.println("\nAffichage de la liste des photos de la faceList");

        FaceList.getFaceOflist("l");



        /*HttpEntity entity = DetectFace.requete(path, detectOnImage);

        JSONObject jsonObject = JsonUtil.httpToJsonObject(entity);

        System.out.println(jsonObject.get("faceId"));*/

        //String jsonString = JsonUtil.httpToString(entity);

        //System.out.println(jsonString);
        //JSONObject json = new JSONObject(jsonString.substring(1,jsonString.length()-1));


        //DetectFace.findSimilar("t1",json.get("faceId").toString());
         //Création d'un fichier à partir du nom de fichier
        //JsonUtil.jsonToFile(jsonString, path);


        //Main pour FaceList

        //FaceList.create("Test Avec 2 photos Yoni et 1 Hocine ", "t1", "yoni");

        //FaceList.getFaceList();

        //FaceList.addFace("src/main/resources/yoni1.jpg","t1","yoni1");
        //FaceList.addFace("src/main/resources/hocine.jpg","t1","hocine");


        //FaceList.getFaceOflist("t1");

        FaceList.deleteFaceList("l");

        //JsonObject j = Json.object().add("name", "Fabrice").add("age", "23");

        //System.out.println(j.toString());



    }
}
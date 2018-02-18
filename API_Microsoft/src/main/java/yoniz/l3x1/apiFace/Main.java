package yoniz.l3x1.apiFace;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.util.JsonUtil;

public class Main {

    public static void main(String[] args) {

        //Main pour DetectFace
        //String detectOnImage = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occ¬lusion,accessories,blur,exposure,noise";
        String detectOnImage = "";


        String path = "src/main/resources/yoni2.jpg";

        HttpEntity entity = DetectFace.requete(path, detectOnImage);

        String jsonString = JsonUtil.httpToString(entity);

        //System.out.println(jsonString);
        JSONObject json = new JSONObject(jsonString.substring(1,jsonString.length()-1));


        DetectFace.findSimilar("t1",json.get("faceId").toString());
         //Création d'un fichier à partir du nom de fichier
        //JsonUtil.jsonToFile(jsonString, path);


        //Main pour FaceList

        //FaceList.create("Test Avec 2 photos Yoni et 1 Hocine ", "t1", "yoni");

        //FaceList.getFaceList();

        //FaceList.addFace("src/main/resources/yoni1.jpg","t1","yoni1");
        //FaceList.addFace("src/main/resources/hocine.jpg","t1","hocine");


        FaceList.getFaceOflist("t1");

        //FaceList.deleteFaceList("list2");

        //JsonObject j = Json.object().add("name", "Fabrice").add("age", "23");

        //System.out.println(j.toString());



    }
}
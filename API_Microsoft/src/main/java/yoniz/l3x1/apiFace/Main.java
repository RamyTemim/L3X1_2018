package yoniz.l3x1.apiFace;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.github.axet.vget.VGet;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.util.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main {

    public static void main(String[] args) {

        JsonObject json1= Json.object().add("photo1","lien1").add("photo2","lien2");

        JsonObject json2 = Json.object().add("video1",json1);

        System.out.println(json2.toString());
        /*{
            "video1": {
                "photo1": "lien1",
                "photo2": "Lien2"
            },
        }

        }*/


        /*//String detectOnImage = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";
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

        JSONObject jsonObject = DetectFace.detect(pathYoniModele, "",false);
        String faceIdModele = jsonObject.get("faceId").toString();

<<<<<<< HEAD
        JSONArray jsonResultat = DetectFace.findSimilar("l", faceIdModele);
=======
        //Voir comment maxNbPersonne fonctionne...
        JSONObject jsonResultat = DetectFace.findSimilar("l", faceIdModele, "1");
>>>>>>> bfbb3a6200542c17b58877955b4cd7ad9a412cff


        System.out.println("\nRetour de detectFace :");
        if(jsonResultat !=null)
            System.out.println(jsonResultat);
        else
            System.out.println("Aucune photo ressemblante trouvé");

        System.out.println("\nAffichage de la liste des photos de la faceList");

        FaceList.getFaceOflist("l");

        FaceList.deleteFaceList("l");
*/

        /*
        //Utilisation des classes de JSON
        //Pour créer un objet Json à parir d'une String (il faut que ça commence par une { sinon c'est un JSONArray )
        JSONObject json = new JSONObject(jsonString.substring(1,jsonString.length()-1));

        //Pour créer un json manuellement :
        JsonObject j = Json.object().add("name", "Fabrice").add("age", "23");
         */

        /*
        //Pour récuperer la liste des faceList
        System.out.println(FaceList.getFaceList());

        //Pour récuperer la liste des photos d'une faceList
        FaceList.getFaceOflist("t1");

        //Pour supprimer une faceList
        FaceList.deleteFaceList("l");
        */

        //System.out.println(DetectFace.detect("https://www.videoindexer.ai/api/Thumbnail/02caeebb58/aa4e5013-180f-47fe-a2d8-c5e9f2788205","",true));


        /*//Pour télécharger des vidéos de youtubes
        try {
            String url = "https://www.youtube.com/watch?v=gRbKmDNJI8g&list=WL&t=0s&index=4";
            String path = "src/main/resources/";
            VGet v = new VGet(new URL(url), new File(path));
            v.download();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/

    }
}
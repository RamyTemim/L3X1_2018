package yoniz.l3x1.Main;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.apiFace.DetectFace;
import yoniz.l3x1.apiFace.FaceList;
import yoniz.l3x1.apiFace.IdAPI;
import yoniz.l3x1.util.JsonUtil;
import yoniz.l3x1.videoIndexer.VideoIndexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main (String []args) throws IOException {

        // Pour récupérer les paths des vidéos se trouvant dans le fichier passe en paramètre
        List<String> videoPath = JsonUtil.ReadFile("src/main/resources/listeVideo.txt");

        // Pour uploader les vidéos se trouvant dans le fichier listeVidéo et récupérer les id des vidéos
        // List<String> videoIds = MethodMain.uploadVideo(videoPath);

        // Pour avoir une liste de vidéos déja indexés sans avoir à les uploader auparavant
        List<String> videoIds = Arrays.asList("8f6aa69ebc","7c26a90418","b84e9c7f30");
        //System.out.println(videoIds.toString());

        //Pour afficher l'ensemble des photos détéctées dans chaque vidéos
        //System.out.println(JsonUtil.getListLienVideo(videoIds));

        // Pour récupérer les paths des photos se trouvant dans le fichier passe en paramètre
        List<String> photoPath = JsonUtil.ReadFile("src/main/resources/listePhoto.txt");

        // Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds,videoPath);

        // Pour lancer la comparaison entre toutes les photos et les vidéos
        JSONObject jsonResultat = MethodMain.detectFaceWithVideoAndPhoto(photoPath,videoIds.size(),videoPath);
        System.out.println(jsonResultat.toString(3));

    }

}

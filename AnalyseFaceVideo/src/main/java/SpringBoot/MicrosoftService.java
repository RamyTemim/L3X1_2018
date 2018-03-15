package SpringBoot;

import microsoft.JsonUtil;
import microsoft.MethodMain;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static SpringBoot.AmazonServices.pathPhoto;
import static SpringBoot.AmazonServices.pathVideo;

@Service
public class MicrosoftService
{

    public String getJSON() throws IOException {

        // Pour récupérer les paths des vidéos se trouvant dans le fichier passe en paramètre
        List<String> videoPath = JsonUtil.readFile(pathVideo);

        // Pour uploader les vidéos se trouvant dans le fichier listeVidéo et récupérer les ids des vidéos
        // List<String> videoIds = MethodMain.uploadVideo(videoPath);
        // System.out.println("identifiants : " + videoIds);
        // Pour avoir une liste de vidéos déja indexés sans avoir à les uploader auparavant
        // List<String> videoIds = Arrays.asList("8f6aa69ebc", "7c26a90418" );
        List<String> videoIds = Arrays.asList("37965bb6c", "93d3a96c02","7269eaf579", "86315daed9", "4d0d0256fa", "16184fdbd7");
        //System.out.println(videoIds.toString());

        //Pour afficher l'ensemble des photos detectes dans chaque vidéo
        //System.out.println(JsonUtil.getListLienVideo(videoIds));

        // Pour récupérer les paths des photos se trouvant dans le fichier passe en paramètre
        List<String> photoPath = JsonUtil.readFile(pathPhoto);

        // Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds, videoPath);

        // Pour lancer la comparaison entre toutes les photos et les vidéos
        JSONObject jsonResultat = MethodMain.detectFaceWithVideoAndPhoto(photoPath, videoIds.size(), videoPath);
        //System.out.println("\n\n"+jsonResultat.toString(2));
        return jsonResultat.toString();
    }

}

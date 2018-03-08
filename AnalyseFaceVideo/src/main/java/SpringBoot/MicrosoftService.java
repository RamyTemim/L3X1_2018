package SpringBoot;

import microsoft.JsonUtil;
import microsoft.MethodMain;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static SpringBoot.Services.pathPhoto;
import static SpringBoot.Services.pathVideo;

@Service
public class MicrosoftService
{

    public String getJSON() throws IOException {
       // Scanner sc = new Scanner(System.in);
        //System.out.println("Veuillez saisir le path pour accéder au fichier contenant les photos :");
        //String pathPhoto = sc.nextLine();

       // sc = new Scanner(System.in);
       // System.out.println("Veuillez saisir le path pour accéder au fichier contenant les vidéos :");
        //String pathVideo = sc.nextLine();

        // Pour récupérer les paths des vidéos se trouvant dans le fichier passe en paramètre
        List<String> videoPath = JsonUtil.readFile(pathVideo);

        // Pour uploader les vidéos se trouvant dans le fichier listeVidéo et récupérer les ids des vidéos
        // List<String> videoIds = MethodMain.uploadVideo(videoPath);

        // Pour avoir une liste de vidéos déja indexés sans avoir à les uploader auparavant
        List<String> videoIds = Arrays.asList("8f6aa69ebc", "7c26a90418");
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

package SpringBoot.service;


import microsoft.JsonUtil;
import microsoft.MethodMain;

import org.json.JSONObject;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class MicrosoftService
{

    public String getJson(List<String> pathPhoto, List<String> pathVideo) throws IOException {

        // Pour uploader les vidéos se trouvant dans le fichier listeVidéo et récupérer les ids des vidéos
         //List<String> videoIds = MethodMain.uploadVideo(pathVideo);

        // Pour avoir une listePhoto de vidéos déja indexés sans avoir à les uploader auparavant
       List<String> videoIds = Arrays.asList("2befe89b87", "44f85eb630", "9b876c9141", "4968f317d4", "4bc3112b90", "d251b38c50", "be854c03a8", "9401dfde10", "178c3c7f94", "f1456c178e", "5671558fb6", "2a4203ae66", "c4adc6488d", "34250c99e3");

        //Pour afficher l'ensemble des photos detectes dans chaque vidéo
        System.out.println("Identifiants des vidéos indexées : " + JsonUtil.getListLienVideo(videoIds));

        // Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds, pathVideo);

        // Pour lancer la comparaison entre toutes les photos et les vidéos
        JSONObject jsonResultat = MethodMain.detectFaceWithVideoAndPhoto(pathPhoto, videoIds.size(), pathVideo);

        return jsonResultat.toString();
    }

}

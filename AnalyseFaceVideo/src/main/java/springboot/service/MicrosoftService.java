package springboot.service;


import springboot.model.MicrosoftModel;
import useful.JsonUtil;
import microsoft.MethodMain;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.*;

@Service
public class MicrosoftService
{

    public MicrosoftModel getJson(List<String> pathPhoto, List<String> pathVideo) throws IOException {



        // Pour avoir une listePhoto de vidéos déja indexés sans avoir à les uploader auparavant
        // List<String> videoIds = Arrays.asList("2befe89b87", "44f85eb630", "9b876c9141", "4968f317d4", "4bc3112b90", "d251b38c50", "be854c03a8", "9401dfde10", "178c3c7f94", "f1456c178e", "5671558fb6", "2a4203ae66", "c4adc6488d", "34250c99e3");
        // List<String> videoIds = Arrays.asList("681ddabc53", "df53b5faca");
        List <String> videoIds = MethodMain.uploadVideo(pathVideo);
        out.println("Identifiants des vidéos indexées : " + JsonUtil.getListLienVideo(videoIds));

        // Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds, pathVideo);


        return MethodMain.detectFaceWithVideoAndPhoto(pathPhoto, videoIds.size(), pathVideo);
    }
}

package springboot.service;


import microsoft.MethodMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import springboot.model.MicrosoftModel;
import useful.Utils;

import java.util.List;

import static java.lang.System.out;

/**
 * L3X1 FACIAL RECONGITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on vidéo)
 * <p>
 * PACKAGE service
 * <p>
 * Cette classe correspond au service du MVC pour tout les traitements correspondant à l'API de Microsoft
 */
@Service
public class MicrosoftService {

    public MicrosoftModel getJson(List<String> pathPhoto, List<String> pathVideo) {

        final Logger log = LogManager.getLogger();

        List<String> videoIds = MethodMain.uploadVideo(pathVideo);
        log.info("Identifiants des vidéos indexées : " + Utils.getListLienVideo(videoIds));

        // Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds, pathVideo);


        return MethodMain.detectFaceWithVideoAndPhoto(pathPhoto, videoIds.size(), pathVideo);
    }
}

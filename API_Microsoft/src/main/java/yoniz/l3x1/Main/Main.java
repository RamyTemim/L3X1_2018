package yoniz.l3x1.Main;

import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.apiFace.DetectFace;
import yoniz.l3x1.apiFace.FaceList;
import yoniz.l3x1.apiFace.IdAPI;
import yoniz.l3x1.util.JsonUtil;
import yoniz.l3x1.videoIndexer.VideoIndexer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main (String []args) {
        //List des paths pour les vidéos
        String path1 = "src/main/resources/film1.mov";
        String path2 = "src/main/resources/film2.mov";
        String path3 = "src/main/resources/film3.mp4";

        //Récupération des videosId renvoyé par l'API video Indexer pour chaque vidéo (ou par upload ou par les vidéos déja indexe)
        //List<String> videoIds = MethodMain.uploadVideo(Arrays.asList(path1, path2));
        List<String> videoIds = Arrays.asList("8f6aa69ebc","7c26a90418","b84e9c7f30");
        System.out.println(videoIds.toString());

        //Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds, Arrays.asList(path1, path2, path3));

        String pathHocine = "src/main/resources/hocine.jpg";
        String pathJordan = "src/main/resources/jordan.jpg";
        String pathYoni = "src/main/resources/yoni1.jpg";

        MethodMain.detectFaceWithVideoAndPhoto(Arrays.asList(pathHocine, pathJordan, pathYoni),videoIds.size());



    }

}

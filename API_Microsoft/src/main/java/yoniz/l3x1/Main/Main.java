package yoniz.l3x1.Main;

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
        //List des paths pour les vidéos

        List<String> videoPath = JsonUtil.ReadFile("src/main/resources/listeVideo.txt");

        //Récupération des videosId renvoyé par l'API video Indexer pour chaque vidéo (ou par upload ou par les vidéos déja indexe)
        List<String> videoIds = MethodMain.uploadVideo(videoPath);
        //List<String> videoIds = Arrays.asList("8f6aa69ebc","7c26a90418","b84e9c7f30");
        System.out.println(videoIds.toString());

        List<String> photoPath = JsonUtil.ReadFile("src/main/resources/listePhoto.txt");
        //Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds,photoPath);


        MethodMain.detectFaceWithVideoAndPhoto(JsonUtil.ReadFile("src/main/resources/listePhoto.txt"),videoIds.size());



    }

}

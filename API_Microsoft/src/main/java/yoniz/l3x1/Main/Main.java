package yoniz.l3x1.Main;

import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.apiFace.DetectFace;
import yoniz.l3x1.apiFace.FaceList;
import yoniz.l3x1.apiFace.IdAPI;
import yoniz.l3x1.videoIndexer.VideoIndexer;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main (String []args) {
        /*//Upload1
        //Upload2
        //Upload3
        FaceList.deleteFaceList("1");
        FaceList.deleteFaceList("2");
        FaceList.deleteFaceList("3");

        //Identifiant des vidéos
        //Yoni
        String videoId1 = "8f6aa69ebc";
        //Yoni et Hocine
        String videoId2 = "7c26a90418";
        //Video du net
        String videoId3 = "b84e9c7f30";



        //VIDEO 1
        //Récupération des métadonnées de la vidéo 1
        JSONObject json1 = VideoIndexer.getBreakdown(videoId1);
        //Création de la liste qui va contenir les url pour accéder aux photos de profil extraite de la vidéo
        List<String> list1 = new ArrayList<String>();
        //Récupération du tableau de face de la vidéo
        JSONArray faces1 = VideoIndexer.getFacesFromVideos(json1);
        //Met dans la list les url pour accéder aux photos extraite de la vidéo
        for (int i =0;i<faces1.length();i++)
        {
            //Remplacer 1 par id
            list1.add(IdAPI.thumbnail.concat(videoId1+"/").concat(faces1.getJSONObject(i).get("thumbnailId").toString()));
        }

        //Création de la faceList qui va contenir les photos extraite de la vidéo
        FaceList.create("Video1", "1", "yoni");
        System.out.println("Ajout des photos dans la liste 1 : ");
        //Ajout des photos dans la faceList
        for (int i =0;i<faces1.length();i++)
        {
            FaceList.addFace(list1.get(i),"1","Video1", true);
        }



        //VIDEO 2
        JSONObject json2 = VideoIndexer.getBreakdown(videoId2);
        List<String> list2 = new ArrayList<String>();
        JSONArray faces2 = VideoIndexer.getFacesFromVideos(json2);
        for (int i =0;i<faces2.length();i++)
        {
            list2.add(IdAPI.thumbnail.concat(videoId2+"/").concat(faces2.getJSONObject(i).get("thumbnailId").toString()));
        }

        FaceList.create("Video2", "2", "yoni");
        System.out.println("\nAjout des photos dans la liste 2 : ");
        for (int i =0;i<faces2.length();i++)
        {
            FaceList.addFace(list2.get(i),"2","Video2",true);
        }



        //VIDEO 3
        JSONObject json3 = VideoIndexer.getBreakdown(videoId3);
        List<String> list3 = new ArrayList<String>();
        JSONArray faces3 = VideoIndexer.getFacesFromVideos(json3);
        for (int i =0;i<faces3.length();i++)
        {
            list3.add(IdAPI.thumbnail.concat(videoId3+"/").concat(faces3.getJSONObject(i).get("thumbnailId").toString()));
        }

        FaceList.create("Video3", "3", "yoni");
        System.out.println("\nAjout des photos dans la liste 3 : ");
        for (int i =0;i<faces3.length();i++)
        {
            FaceList.addFace(list3.get(i),"3","Video3",true);
        }

        //AFFICHAGE DU CONTENU DE CHAQUE FACELIST
        System.out.println("\nListe 1 :");
        System.out.println(FaceList.getFaceOflist("1").toString(2));
        System.out.println("\nListe 2 :");
        System.out.println(FaceList.getFaceOflist("2").toString(2));
        System.out.println("\nListe 3 :");
        System.out.println(FaceList.getFaceOflist("2").toString(2));
*/

        String pathHocine = "src/main/resources/hocine.jpg";
        String pathJordan = "src/main/resources/jordan.jpg";
        String pathYoni = "src/main/resources/yoni1.jpg";

        //HOCINE
        //Analyse de l'ensemble des faceList avec la photo de Hocine
        //Récupération du FaceId de Hocine
        System.out.println("\nPhoto de Hocine : ");
        JSONObject jsonObject1 = DetectFace.detect(pathHocine, "",false);
        String faceIdHocine = jsonObject1.get("faceId").toString();
        //Boucle permettant d'analyser les 3 FaceList de chaque vidéo pour la photo de Hocine
        for (int i = 1 ;i <= 3;i++)
        {
            JSONArray jsonResultat = DetectFace.findSimilar(String.valueOf(i), faceIdHocine, 20);
            if(jsonResultat!=null)
                System.out.println("Pour la vidéo " + i + ":\n"+jsonResultat.toString(2));
        }

        //JORDAN
        //Analyse de l'ensemble des faceList avec la photo de Jordan
        //Récupération du FaceId de Jordan
        System.out.println("\nPhoto de Jordan : ");
        JSONObject jsonObject2 = DetectFace.detect(pathJordan, "",false);
        String faceIdJordan = jsonObject2.get("faceId").toString();
        //Boucle permettant d'analyser les 3 FaceList de chaque vidéo pour la photo de Hocine
        for (int i = 1 ;i <= 3;i++)
        {
            JSONArray jsonResultat = DetectFace.findSimilar(String.valueOf(i), faceIdJordan, 20);
            if(jsonResultat!=null)
                System.out.println("Pour la vidéo " + i + ":\n"+jsonResultat.toString(2));
        }

        //YONI
        //Analyse de l'ensemble des faceList avec la photo de Yoni
        //Récupération du FaceId de Yoni
        System.out.println("\nPhoto de Yoni : ");
        JSONObject jsonObject3 = DetectFace.detect(pathYoni, "",false);
        String faceIdYoni = jsonObject3.get("faceId").toString();
        //Boucle permettant d'analyser les 3 FaceList de chaque vidéo pour la photo de Hocine
        for (int i = 1 ;i <= 3;i++)
        {
            JSONArray jsonResultat = DetectFace.findSimilar(String.valueOf(i), faceIdYoni, 20);
            if(jsonResultat!=null)
                System.out.println("Pour la vidéo " + i + ":\n"+jsonResultat.toString(2));
        }


    }

}

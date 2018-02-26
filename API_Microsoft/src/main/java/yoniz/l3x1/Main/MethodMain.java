package yoniz.l3x1.Main;

import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.apiFace.DetectFace;
import yoniz.l3x1.apiFace.FaceList;
import yoniz.l3x1.apiFace.IdAPI;
import yoniz.l3x1.util.JsonUtil;
import yoniz.l3x1.videoIndexer.VideoIndexer;

import java.util.ArrayList;
import java.util.List;

public class MethodMain {
    
    public static List<String> uploadVideo(List<String> pathVideo)
    {
        //videoId est la liste qui va contenir la liste des Id des vidéos que videoIndexer va renvoyer
        List <String> videoIds = new ArrayList<String>();
        //On rajoute dans la liste le retour de l'upload de videoIndexer
        for (String path : pathVideo) {
            videoIds.add(JsonUtil.supprimeGuillemet(VideoIndexer.upload(path)));
        }

        int nbVideoUpload = 0;
        //Tant que toutes les vidéos n'ont pas finis d'être uploadé on attend
        while (nbVideoUpload != videoIds.size()) {
            nbVideoUpload = 0;
            for (int i = 0; i<videoIds.size();i++){
                JSONObject json = VideoIndexer.getProcessingState(videoIds.get(i));
                System.out.println("Video " + i + " : " +json);
                if (json.get("state").toString().equals("Processed"))
                {
                   nbVideoUpload++;
                }
                else if(json.get("state").toString().equals("Failed"))
                {
                    System.out.println("Erreur lors de l'upload des vidéos : ");
                    System.exit(-1);
                }
            }
        }
        System.out.println("Vidéos uploadées avec succès");
        return videoIds;
    }

    public static void createFaceListFromPhotosOnVideo (List<String> videoIds, List<String> pathVideos)
    {
        for(int i = 0 ; i< videoIds.size() ; i++) {
            FaceList.deleteFaceList(String.valueOf(i));
            //Récupération des métadonnées de la vidéo
            JSONObject json = VideoIndexer.getBreakdown(videoIds.get(i));
            //Création de la liste qui va contenir les urls pour accéder aux photos de profils extraite de la vidéo
            List<String> listUrlPhoto = new ArrayList<String>();
            //Récupération du tableau JSON de face de la vidéo
            JSONArray faces = VideoIndexer.getFacesFromVideos(json);
            //Met dans la liste les url pour accéder aux photos extraite de la vidéo
            for (int j = 0; j < faces.length(); j++) {
                listUrlPhoto.add(IdAPI.thumbnail.concat(videoIds.get(i) + "/")
                                                .concat(faces.getJSONObject(j).get("thumbnailId").toString()));
            }

            //Création de la faceList qui va contenir les photos extraite de la vidéo
            FaceList.create(JsonUtil.pathToName(pathVideos.get(i)), String.valueOf(i), "yoni");
            System.out.println("Ajout des photos dans la liste " + i+1 + " : ");
            //Ajout des photos dans la faceList
            for (int j = 0; j < faces.length(); j++) {
                FaceList.addFace(listUrlPhoto.get(j), String.valueOf(i), "Photo ".concat(String.valueOf(j+1)), true);
            }

            System.out.println("\nListe "+ (i+1) + ": ");
            System.out.println(FaceList.getFaceOflist(String.valueOf(i)).toString(2));
        }
    }

    public static void detectFaceWithVideoAndPhoto (List <String> photosPath, int nbVideos)
    {
        for(int i = 0; i< photosPath.size() ; i++) {
            //Analyse de l'ensemble des faceList avec la photo i
            //Récupération du FaceId de i
            System.out.println("\nPhoto de " + JsonUtil.pathToName(photosPath.get(i)));
            JSONObject jsonObject = DetectFace.detect(photosPath.get(i), "", false);
            String faceId = jsonObject.get("faceId").toString();
            //Boucle permettant d'analyser les FaceList de chaque vidéo pour la photo i
            for (int j = 0; j < nbVideos; j++)
            {
                JSONArray jsonResultat = DetectFace.findSimilar(String.valueOf(j), faceId);
                if (jsonResultat != null)
                    System.out.println("Pour la vidéo " + (j+1) + ":\n" + jsonResultat.toString(2));
                else
                    System.out.println("Pour la vidéo " + (j+1) + ":\n[]");
            }
        }
    }
}

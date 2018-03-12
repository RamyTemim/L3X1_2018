package microsoft;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MethodMain {

    /**
     * Méthode permettant d'uploader une liste de vidéos et d'attendre que l'API de microsoft ait terminé de les indexer
     * @param pathVideo Une liste de lien vers les vidéos stockées localement
     * @return Une liste de videoId correspondant à l'Id permettant d'accéder aux vidéos indexées sur le cloud de Microsoft
     */
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
                System.out.println("Video " + (i+1) + " : " +json);
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

    /**
     * Méthode qui va :
     *      extraire les photos que l'API de Microsoft aura détécter sur les photos
     *      stocker les urls des photos extraite dans une List
     *      Créer une faceList pour chaque vidéo et stocker les photos extraites de chaque video dans la faceList correspondante
     * @param videoIds List des videoId pour chaque vidéo permettant d'accéder à la vidéo qui a été indexe sur le cloud
     * @param pathVideos La liste des chemins pour accéder aux vidéos (pour en extraire le nom)
     */
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
            //System.out.println("Ajout des photos dans la liste " + (i+1) + " : ");
            //Ajout des photos dans la faceList
            for (int j = 0; j < faces.length(); j++) {
                FaceList.addFace(listUrlPhoto.get(j), String.valueOf(i), "Photo ".concat(String.valueOf(j+1)), true);
            }

            //System.out.println("\nListe "+ (i+1) + ": ");
            //System.out.println(FaceList.getFaceOflist(String.valueOf(i)).toString(2));
        }
    }

    /**
     * Méthode permettant de prendre chaque photo de profil, la comparer avec chaque liste de photo de profil extraite des vidéos,
     * et renvoyer un fichier Json qui va afficher pour chaque photo les vidéos ou celle-ci apparait
     * @param photosPath La liste des photos de profil à analyser
     * @param nbVideos le nombre de vidéos avec lesquelle il faudra comparer chaque vidéo
     * @param videosPath La liste des vidéos à analyser
     * @return Un Objet Json contenant pour chaque photo la liste des vidéos ou celle ci apparait
     */
    public static JSONArray detectFaceWithVideoAndPhoto (List <String> photosPath, int nbVideos, List <String> videosPath)
    {
        // Un tableau json qui va contenir la liste des photos avec leur vidéos
        JSONObject jsonObjectListDePhoto = new JSONObject();
        for(int i = 0; i< photosPath.size() ; i++) {
            //Analyse de l'ensemble des faceList avec la photo i
            //Récupération du FaceId de i
            JSONObject jsonObject = DetectFace.detect(photosPath.get(i), "", false);
            if(jsonObject==null)
            {
                System.out.println("La photo "+i+" n'est pas détécté comme un humain");
                System.exit(-1);
            }
            String faceId = jsonObject.get("faceId").toString();

            //Un tableau qui va contenir la liste de vidéo pour la photo i
            JSONArray jsonArrayVideoDePhoto = new JSONArray();
            //Boucle permettant d'analyser les FaceList de chaque vidéo pour la photo i
            for (int j = 0; j < nbVideos; j++) {
                JSONArray jsonResultat = DetectFace.findSimilar(String.valueOf(j), faceId);

                if(!jsonResultat.toString().equals("[]"))
                    jsonArrayVideoDePhoto.put(JsonUtil.pathToName(videosPath.get(j)));

            }
            jsonObjectListDePhoto.put(JsonUtil.pathToName(photosPath.get(i)), jsonArrayVideoDePhoto);
        }
        //L'objet qui va contenir le résultat avec microsoft comme key et la liste des photos et vidéos en parametre
<<<<<<< HEAD
        //JSONObject jsonObjectMicrosoft = new JSONObject().put("Microsoft",jsonArrayListDePhoto);
        //return jsonObjectMicrosoft;
        return jsonArrayListDePhoto;
=======
        return jsonObjectListDePhoto;
>>>>>>> 74e45c63aeb7f6f4117a2b91b5e2c9de74d90bf6
    }
}

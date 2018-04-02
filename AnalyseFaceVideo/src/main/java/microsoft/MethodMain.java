package microsoft;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MethodMain {

    /**
     * Méthode permettant d'uploader une liste de vidéos et d'attendre que l'API de microsoft ait terminé de les indexer
     * @param pathVideo Une listePhoto de lien vers les vidéos stockées localement
     * @return Une listePhoto de videoId correspondant à l'Id permettant d'accéder aux vidéos indexées sur le cloud de Microsoft
     */
    public static List<String> uploadVideo(List<String> pathVideo) throws IOException {
        //videoIds est la liste qui va contenir les Id des vidéos que videoIndexer va indéxer
        List <String> videoIds = new ArrayList<>();
        System.out.println("videos en cours d'upload");
        //On rajoute dans la liste le retour de l'upload de videoIndexer
        for (String path : pathVideo) {
            videoIds.add(JsonUtil.supprimeGuillemet(VideoIndexer.upload(path)));
        }

        System.out.println("Vidéos en cours d'indexation");
        int nbVideoUpload = 0;
        //Tant que toutes les vidéos n'ont pas finis d'être uploadé on attend
        while (nbVideoUpload != videoIds.size()) {
            nbVideoUpload = 0;
            for (int i = 0; i<videoIds.size();i++){

                JSONObject json = VideoIndexer.getProcessingState(videoIds.get(i));
                System.out.println("Video " + (i+1) + " : " +json);
                if(json.has("state"))
                {
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
        }
        System.out.println("Vidéos uploadées avec succès");
        return videoIds;
    }

    /**
     * Méthode qui va :
     *      extraire les photos que l'API de Microsoft aura détécter sur les photos
     *      stocker les urls des photos extraite dans une List
     *      Créer une faceList pour chaque vidéo et stocker les photos extraites de chaque video dans la faceList correspondante
     * @param videoIds Liste des identifiants des vidéos stockées sur le cloud de microsoft
     * @param pathVideos La liste des chemins pour accéder aux vidéos localement (pour en extraire le nom)
     */
    public static void createFaceListFromPhotosOnVideo (List<String> videoIds, List<String> pathVideos) throws IOException {
        for(int i = 0 ; i< videoIds.size() ; i++) {
            FaceList.deleteFaceList(String.valueOf(i));

            //Récupération des métadonnées de la vidéo
            JSONObject json = VideoIndexer.getBreakdown(videoIds.get(i));

            //Création de la listePhoto qui va contenir les urls pour accéder aux photos de profils extraite de la vidéo
            List<String> listUrlPhoto = new ArrayList<>();

            //Récupération du tableau JSON de face de la vidéo
            JSONArray faces = VideoIndexer.getFacesFromVideos(json);

            //Met dans la listePhoto les url pour accéder aux photos extraite de la vidéo
            for (int j = 0; j < faces.length(); j++) {
                listUrlPhoto.add(IdAPI.thumbnail.concat(videoIds.get(i) + "/")
                                                .concat(faces.getJSONObject(j).get("thumbnailId").toString()));
            }

            //Création de la faceList qui va contenir les photos extraite de la vidéo
            FaceList.createFaceList(JsonUtil.pathToName(pathVideos.get(i)), String.valueOf(i), "yoni");

            System.out.println("Ajout des photos dans la listePhoto " + (i+1) + " : ");

            //Ajout des photos dans la faceList
            for (int j = 0; j < faces.length(); j++) {
                FaceList.addFace(listUrlPhoto.get(j), String.valueOf(i), "Photo ".concat(String.valueOf(j+1)), true);
            }

            System.out.println("\nListe "+ (i+1) + ": ");
            System.out.println(FaceList.getFaceOflist(String.valueOf(i)).toString(2));
        }
    }

    /**
     * Méthode permettant de prendre chaque photo de profil, la comparer avec chaque photo de profil extraite des vidéos,
     * et renvoyer un fichier Json qui va afficher pour chaque photo les vidéos ou celle-ci apparait
     * @param photosPath La listePhoto des photos de profil à analyser
     * @param nbVideos le nombre de vidéos avec lesquelle il faudra comparer chaque vidéo
     * @param videosPath La listePhoto des vidéos à analyser
     * @return Un Objet Json contenant pour chaque photo la listePhoto des vidéos ou celle ci apparait
     */
    public static JSONObject detectFaceWithVideoAndPhoto (List <String> photosPath, int nbVideos, List <String> videosPath)
    {
        // Un tableau json qui va contenir la listePhoto des photos avec leur vidéos
        JSONObject jsonObjectListDePhoto = new JSONObject();
        for(int i = 0; i< photosPath.size() ; i++) {

            JSONObject jsonObject = DetectFace.detectFace(photosPath.get(i), "", false);

            if(jsonObject==null)
            {
                System.out.println("La photo "+i+" n'est pas détécté comme un humain");
                System.exit(-1);
            }

            String faceId = jsonObject.get("faceId").toString();

            //Un tableau qui va contenir la listePhoto de vidéo pour la photo i
            JSONArray jsonArrayVideoDePhoto = new JSONArray();
            //Boucle permettant d'analyser les FaceList de chaque vidéo pour la photo i
            for (int j = 0; j < nbVideos; j++) {
                JSONArray jsonResultat = DetectFace.findSimilarFace(String.valueOf(j), faceId);
                if(!jsonResultat.toString().equals("[]"))
                    jsonArrayVideoDePhoto.put(JsonUtil.pathToName(videosPath.get(j)));

            }
            jsonObjectListDePhoto.put(JsonUtil.pathToName(photosPath.get(i)), jsonArrayVideoDePhoto);
        }

        return jsonObjectListDePhoto;
    }
}
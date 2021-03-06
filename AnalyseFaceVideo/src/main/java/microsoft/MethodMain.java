package microsoft;

import identification.KeyMicrosoftApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import springboot.model.MicrosoftModel;
import springboot.model.Persons;
import useful.Utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE microsoft
 * <p>
 * Cette classe contient les méthodes principales qui vont permettre de faire les liens
 * entre les différentes APIs et service de microsoft
 */
public class MethodMain {
    private MethodMain() {
    }

    public static final Logger log = LogManager.getLogger();

    private static final String STATE = "state";

    /**
     * Méthode permettant d'uploader une liste de vidéos et d'attendre que l'API de microsoft ait terminé de les indexer
     *
     * @param pathVideo Une listePhoto de lien vers les vidéos stockées localement
     * @return Une listePhoto de videoId correspondant à l'Id permettant d'accéder aux vidéos indexées sur le cloud de Microsoft
     */
    public static List<String> uploadVideo(List<String> pathVideo) {
        //videoIds est la liste qui va contenir les Id des vidéos que videoIndexer va indéxer
        List<String> videoIds = new ArrayList<>();
        log.info("Vidéos en cours d'upload");
        //On rajoute dans la liste le retour de l'upload de videoIndexer
        for (String path : pathVideo) {
            videoIds.add(VideoIndexer.upload(path));
        }

        log.info("Vidéos en cours d'indexation");
        int nbVideoUpload = 0;
        //Tant que toutes les vidéos n'ont pas finis d'être uploadé on attend
        while (nbVideoUpload != videoIds.size()) {
            nbVideoUpload = 0;
            for (int i = 0; i < videoIds.size(); i++) {

                JSONObject json = VideoIndexer.getProcessingState(videoIds.get(i));
                log.info("Video " + (i + 1) + " : " + json);
                if (json.has(STATE)) {
                    if (json.get(STATE).toString().equals("Processed")) {
                        nbVideoUpload++;
                    } else if (json.get(STATE).toString().equals("Failed")) {
                        log.info("Erreur lors de l'upload des vidéos : ");
                        exit(-1);
                    }
                }
            }
        }
        log.info("Vidéos uploadées avec succès");
        return videoIds;
    }

    /**
     * Méthode qui va :
     * extraire les photos que l'API de Microsoft aura détécter sur les photos
     * stocker les urls des photos extraite dans une List
     * Créer une faceList pour chaque vidéo et stocker les photos extraites de chaque video dans la faceList correspondante
     *
     * @param videoIds   Liste des identifiants des vidéos stockées sur le cloud de microsoft
     * @param pathVideos La liste des chemins pour accéder aux vidéos localement (pour en extraire le nom)
     */
    public static void createFaceListFromPhotosOnVideo(List<String> videoIds, List<String> pathVideos) {
        for (int i = 0; i < videoIds.size(); i++) {
            FaceList.deleteFaceList(String.valueOf(i));

            //Récupération des métadonnées de la vidéo
            JSONObject json = null;
            json = VideoIndexer.getBreakdown(videoIds.get(i));
            //Création de la listePhoto qui va contenir les urls pour accéder aux photos de profils extraite de la vidéo
            List<String> listUrlPhoto = new ArrayList<>();

            //Récupération du tableau JSON de face de la vidéo
            JSONArray faces = VideoIndexer.getFacesFromVideos(json);

            //Met dans la listePhoto les url pour accéder aux photos extraite de la vidéo
            for (int j = 0; j < faces.length(); j++) {
                listUrlPhoto.add(KeyMicrosoftApi.THUMBNAIL.concat(videoIds.get(i) + "/")
                        .concat(faces.getJSONObject(j).get("thumbnailId").toString()));
            }

            //Création de la faceList qui va contenir les photos extraite de la vidéo
            FaceList.createFaceList(Utils.pathToName(pathVideos.get(i)), String.valueOf(i));

            log.info("Ajout des photos dans la listePhoto " + (i + 1) + " : ");

            //Ajout des photos dans la faceList
            for (int j = 0; j < faces.length(); j++) {
                FaceList.addFace(listUrlPhoto.get(j), String.valueOf(i), "Photo ".concat(String.valueOf(j + 1)));
            }

            log.info("\nListe " + (i + 1) + ": ");
            log.info(FaceList.getFaceOflist(String.valueOf(i)).toString(2));
        }
    }

    /**
     * Méthode permettant de prendre chaque photo de profil, la comparer avec chaque photo de profil extraite des vidéos,
     * et renvoyer un fichier Json qui va afficher pour chaque photo les vidéos ou celle-ci apparait
     *
     * @param photosPath La listePhoto des photos de profil à analyser
     * @param nbVideos   le nombre de vidéos avec lesquelle il faudra comparer chaque vidéo
     * @param videosPath La listePhoto des vidéos à analyser
     * @return Un Objet Json contenant pour chaque photo la listePhoto des vidéos ou celle ci apparait
     */
    public static MicrosoftModel detectFaceWithVideoAndPhoto(List<String> photosPath, int nbVideos, List<String> videosPath) {
        MicrosoftModel model = new MicrosoftModel();

        for (int i = 0; i < photosPath.size(); i++) {

            Persons person = new Persons();
            JSONObject jsonObject = DetectFace.detectFace(photosPath.get(i));

            if (jsonObject == null) {
                log.info("La photo " + i + " n'est pas détécté comme un humain");
                exit(-1);
            }

            String faceId = jsonObject.get("faceId").toString();

            //Un tableau qui va contenir la listePhoto de vidéo pour la photo i
            List<String> list = new ArrayList<>();
            //Boucle permettant d'analyser les FaceList de chaque vidéo pour la photo i
            for (int j = 0; j < nbVideos; j++) {
                JSONArray jsonResultat = DetectFace.findSimilarFace(String.valueOf(j), faceId);
                if (!jsonResultat.toString().equals("[]")) {
                    list.add(Utils.pathToName(videosPath.get(j)));
                }

            }
            person.setName(Utils.pathToName(photosPath.get(i)));
            person.setVideos(list);
            model.addPerson(person);
        }
        return model;
    }
}

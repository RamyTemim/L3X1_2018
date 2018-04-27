/*
 * L3X1 FACIAL RECOGNITION COMPARATOR
 *
 * IA as a service (Facial recognition on video)
 *
 * PACKAGE AMAZON
 *
 * Classe qui fait ofice d'une boite a outils. Elle contient
 * divert méthode qui permettent de manipuler des fichiers( lire
 * un fichier, écrir dans un fichier...)
 *
 * elle permet aussi de manipuller des objets JSON
 *
 * Elle est utiliser pratiquement dans toutes les classes
 */
package useful;

import identification.KeyMicrosoftApi;
import microsoft.VideoIndexer;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * L3X1 FACIAL RECONGITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on vidéo)
 * <p>
 * PACKAGE useful
 * <p>
 * Cette classe correspond à une boite à outil pour tout ce qui n'est pas inclus dans les autre méthodes
 * comme la manipulation du JSON
 */
public class Utils {

    private Utils() {
    }


    public static final Logger log = LogManager.getLogger();

    /**
     * <p>
     * Methode pour tester si l'utilisateur a rentre les fichiers contenant
     * les pĥotos et celui contenant les videos au bon endroit
     * Elle verifie aussi que le fichier contenant les photos contient que des liens
     * vers des photos et celui contenant des vidéos que des liens vers des videos
     * @param list la liste qui contient les chemins
     * @param type pour savoir a quelle vérification faire appel
     */
    public static void checkLink(List<String> list, String type)
    {
        if (type.equals("photo"))
        {
            for (String aList : list)
            {
                File file = new File(aList);
                String mimetype = new MimetypesFileTypeMap().getContentType(file);
                String typeFile = mimetype.split("/")[0];
                if (!typeFile.equals("image"))
                {
                    log.info("le chemin que vous avez donner n'est pas un chemin vers une photo");
                    System.exit(1);
                }
            }
        }

        if (type.equals("video"))
        {
            for(String aList : list)
            {
                File file = new File(aList);
                String mimetype = new MimetypesFileTypeMap().getContentType(file);
                String typeFile = mimetype.split("/")[0];
                if (!typeFile.equals("video"))
                {
                    log.info("le chemin que vous avez donner n'est pas un chemin vers une video");
                    System.exit(1);
                }
            }
        }

    }


    /**
     * Méthode pour faire un temps d'attente.
     * cette méthode est utiliser dans le get du controller
     * pour attendre que les fichiers qui contiennent les paths arrivent
     */
    public static void sleepGet() {
        log.info("Attente de fichier ");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.warn(e);
            Thread.currentThread().interrupt();
        }

    }

    /**
     * Méthode qui prend le fichier envoyé par l'utilisateur et copie son contenu
     * dans le fichier listePhotos
     *
     * @param multipartFile fichier reçu dans une requete HTTP
     * @return un fichier qui contient le contunue du multipartFile
     */
    public static File storeFile(MultipartFile multipartFile, String type )
    {
        File file = null;

        if (type.equals("photo"))
        {
            file = new File("listePhoto");
            try {
                byte[] bytes = multipartFile.getBytes();
                Path path = Paths.get("listePhoto");
                Files.write(path, bytes);
            } catch (Exception e) {
                log.info(e);
            }
        }

        if (type.equals("video"))
        {
            file = new File("listeVideo");
            try {
                byte[] bytes = multipartFile.getBytes();
                Path path = Paths.get("listeVideo");
                Files.write(path, bytes);
            } catch (Exception e) {
                log.info(e);
            }
        }
        return file;
    }


    /**
     * Methode qui lis un fichier ligne par ligne pour récupérer
     * les paths que l'utilisateur a écrit dans un fichier
     * et chaque fois qu'il lit une ligne qui est un path il récupére
     * le chemin absolu pour accéder au fichier qu'il y a dans le path
     *
     * @param file fichier a lire
     * @return listeOfpaths : listePhoto de string qui contient les lignes du fichier
     */
    public static List<String> readFile(File file) {
        String path;
        List<String> listeOfpaths = new ArrayList<>();
        if (file.exists()) {
            // test si on peut lire le fichier
            if (file.canRead()) {
                try (BufferedReader buffer = new BufferedReader(new FileReader(file))) {
                    while ((path = buffer.readLine()) != null) {
                        File fileForAbsolutePath = new File(path);
                        if(fileForAbsolutePath.exists()) {
                            String absolutePath = fileForAbsolutePath.getAbsolutePath();
                            listeOfpaths.add(absolutePath);
                        }else {
                            log.info("la photo ou la vidéo n'existe pas");
                            System.exit(1);
                        }
                    }
                } catch (IOException e) {
                    log.info("Erreur lors de la lecture du fichier dans la méthode readFile : ");
                }
            } else {
                log.info("Le fichier ne peut pas être lu ");
            }
        } else {
            log.info("Le fichier n'existe pas ");
        }
        return listeOfpaths;
    }


    /**
     * Méthode permettant à partir d'une entité HTTP de récupérer le fichier JSON contenu à l'intérieur
     *
     * @param entity L'entitée HTTP à extraire
     * @return Un objet JSON qui était présent dans l'entité HTTP
     */
    public static JSONObject httpToJsonObject(HttpEntity entity) {
        JSONObject jsonObject = null;
        if (entity != null) {
            try {
                String jsonString = EntityUtils.toString(entity).trim();
                jsonObject = stringToJson(jsonString);
            } catch (IOException e) {
                log.info("erreur lors de la lecture du fichier pour la methode httpToJsonObject" + e);
            }

        }
        return jsonObject;
    }

    /**
     * Méthode permettant de transformer une chaine de caractère en JSONObject
     * ou JsonArray en fonction de la chaine de caractère
     *
     * @param jsonString La chaine de caractère à transformer en Objet JSON
     * @return Un objet JSON correspondant à la chaine de caractère passée en paramètre
     */
    private static JSONObject stringToJson(String jsonString) {
        JSONObject jsonObject = null;
        if (jsonString.charAt(0) == '[') {
            jsonObject = new JSONObject(jsonString.substring(1, jsonString.length() - 1));
        } else if (jsonString.charAt(0) == '{') {
            jsonObject = new JSONObject(jsonString);
        }
        return jsonObject;

    }

    /**
     * Méthode permettant de récupérer le nom d'un fichier à partir de son path
     *
     * @param path le path du fichier que l'on veut extraire le nom
     * @return le nom du fichier correspondant au path
     */
    public static String pathToName(String path) {
        return path.substring(path.lastIndexOf('/') + 1, path.lastIndexOf('.'));
    }

    /**
     * Méthode permettant de supprimer les guillemets d'une chaine de caractère
     *
     * @param mot le String auquel on veut supprimer les guillemets
     * @return un String auquel on a enlevé les guillemets
     */
    public static String supprimeGuillemet(String mot) {
        return mot.substring(1, mot.length() - 1);
    }

    /**
     * Méthode qui renvoit une liste de liens permettant d'accéder au photos extraites de chaque vidéo
     *
     * @param videoIds List de videoId permettant d'accéder aux vidéos indexées dans le cloud
     * @return Une liste qui va contenir pour chaque membre de sa liste une liste de lien pour accéder aux photos extraite des vidéos
     */
    public static List<List<String>> getListLienVideo(List<String> videoIds) {
        List<List<String>> listLien = new ArrayList<>();
        for (String videoId : videoIds) {
            JSONObject json = VideoIndexer.getBreakdown(videoId);
            List<String> listUrlPhoto = new ArrayList<>();
            //Met dans la listePhoto les url pour accéder aux photos extraite de la vidéo
            JSONArray faces = VideoIndexer.getFacesFromVideos(json);

            for (int j = 0; j < faces.length(); j++) {
                listUrlPhoto.add(KeyMicrosoftApi.THUMBNAIL.concat(videoId + "/")
                        .concat(faces.getJSONObject(j).get("thumbnailId").toString()));
            }
            listLien.add(listUrlPhoto);
        }
        return listLien;
    }

}
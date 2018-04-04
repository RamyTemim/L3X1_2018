package microsoft;


import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
private JsonUtil(){}
    private static Logger log = LogManager.getLogger();
    public static void sleepGet() throws InterruptedException {
       log.info("Attente de fichier ");
        Thread.sleep(5000);

    }

    /**
     *
     * @param multipartFile  fichier reçu dans une requete HTTP
     * @return un fichier qui contient le contunue du multipartFile
     */
    public static File storeFilePhoto(MultipartFile multipartFile)
    {
        File file=new File("src/resources/listePhoto");
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path =Paths.get("src/resources/listePhoto");
            Files.write(path,bytes);
        } catch (Exception e) {
            log.info(e);
        }
        return file;
    }



    public static File storeFileVideo(MultipartFile multipartFile)
    {
        File file= new File("src/resources/listeVideo");
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path =Paths.get("src/resources/listeVideo");
            Files.write(path,bytes);
        } catch (Exception e) {
            log.info(e);
        }
        return file;
    }


    /**
     * Methode qui lis un fichier ligne par ligne
     * @param file fichier a lire
     * @return  listeOfpaths : listePhoto de string qui contien les ligne du fichier
     */
    public static List<String> readFile (File file) throws IOException {
        String path;
        List<String> listeOfpaths = new ArrayList<>();
        if (file.exists()) {
            // test si on peut lire le fichier
            if (file.canRead())
            {
                try( BufferedReader buffer = new BufferedReader(new FileReader(file))) {
                    while ((path = buffer.readLine()) != null) {
                        listeOfpaths.add(path);
                    }
                }
            } else
            {
                log.info("Le fichier ne peut pas être lu ");
            }
        }else {
            log.info("Le fichier n'existe pas ");
        }
        return listeOfpaths;
    }


    /**
     * Méthode permettant à partir d'une entité HTTP de récupérer le fichier JSON contenu à l'intérieur
     * @param entity L'entitée HTTP à extraire
     * @return Un objet JSON qui était présent dans l'entité HTTP
     */
    public static JSONObject httpToJsonObject(HttpEntity entity)
    {
        JSONObject jsonObject = null;
        if (entity!=null)
        {
            try {
                String jsonString=EntityUtils.toString(entity).trim();
                jsonObject = stringToJson(jsonString);
            }
            catch (IOException e) {
                log.info("erreur lors de la lecture du fichier pour la methode httpToJsonObject" + e);
            }

        }
        return jsonObject;
    }

    /**
     * Méthode permettant de transformer une chaine de caractère en JSONObject ou JsonArray en fonction de la chaine de caractère
     * @param jsonString La chaine de caractère à transformer en Objet JSON
     * @return Un objet JSON correspondant à la chaine de caractère passée en paramètre
     */
    private static JSONObject stringToJson (String jsonString)
    {
        JSONObject jsonObject=null;
        if (jsonString.charAt(0) == '[') {
            jsonObject = new JSONObject(jsonString.substring(1,jsonString.length()-1));
        }
        else if (jsonString.charAt(0) == '{') {
            jsonObject = new JSONObject(jsonString);
        }
        return jsonObject;

    }

    /**
     * Méthode permettant de récupérer le nom d'un fichier à partir de son path
     * @param path le path du fichier que l'on veut extraire le nom
     * @return le nom du fichier correspondant au path
     */
    public static String pathToName(String path)
    {
        return path.substring(path.lastIndexOf('/')+1, path.lastIndexOf('.'));
    }

    /**
     * Méthode permettant de supprimer les guillemets d'une chaine de caractère
     * @param mot le String auquel on veut supprimer les guillemets
     * @return un String auquel on a enlevé les guillemets
     */
    public static String supprimeGuillemet(String mot)
    {
        return mot.substring(1,mot.length()-1);
    }

    /**
     * Méthode qui renvoit une liste de liens permettant d'accéder au photos extraites de chaque vidéo
     * @param videoIds List de videoId permettant d'accéder aux vidéos indexées dans le cloud
     * @return Une liste qui va contenir pour chaque membre de sa liste une liste de lien pour accéder aux photos extraite des vidéos
     */
    public static List<List<String>> getListLienVideo (List<String> videoIds ) throws IOException {
        List<List<String>> listLien = new ArrayList<>();
        for(int i = 0; i<videoIds.size(); i++)
        {
            JSONObject json = VideoIndexer.getBreakdown(videoIds.get(i));
            List<String> listUrlPhoto = new ArrayList<>();
            //Met dans la listePhoto les url pour accéder aux photos extraite de la vidéo
            JSONArray faces = VideoIndexer.getFacesFromVideos(json);

            for (int j = 0; j < faces.length(); j++) {
                listUrlPhoto.add(IdAPI.THUMBNAIL.concat(videoIds.get(i) + "/")
                        .concat(faces.getJSONObject(j).get("thumbnailId").toString()));
            }
            listLien.add(listUrlPhoto);
        }
        return listLien;
    }

}
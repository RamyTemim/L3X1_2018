package microsoft;


import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    /**
     * Lis un fichier texte qui contient les liens. Chaque ligne dans le fichier est un lien.
     * @param pathTofile le chemin vers le fichier qui contient les liens
     * @return une liste se String qui contient les liens
     * @throws IOException si le fichier n'a paspu etres créer
     */
    public static List<String> readFile (String pathTofile) throws IOException
    {
        String path;
        List<String> listeOfpaths = new ArrayList<>();
        File file = new File(pathTofile);
        //teste si le fichier existe
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

                System.err.println("Le fichier ne peut pas être lu ");
            }
        }else {
            System.err.println("Le fichier n'existe pas ");
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
                e.printStackTrace();
            }

        }
        return jsonObject;
    }

    /**
     * Méthode permettant de transformer une chaine de caractère en Objet JSON
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
        return path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
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
     * @param videoIds List de videoId permettant d'accéder aux vidéos indexe dans le cloud
     * @return Une list qui va contenir pour chaque membre de sa liste une list de lien pour accéder aux photos
     */
    public static List<List<String>> getListLienVideo (List<String> videoIds )
    {
        List<List<String>> listLien = new ArrayList<List<String>>();
        for(int i = 0; i<videoIds.size(); i++)
        {
            JSONObject json = VideoIndexer.getBreakdown(videoIds.get(i));
            List<String> listUrlPhoto = new ArrayList<String>();
            //Met dans la liste les url pour accéder aux photos extraite de la vidéo
            JSONArray faces = VideoIndexer.getFacesFromVideos(json);

            for (int j = 0; j < faces.length(); j++) {
                listUrlPhoto.add(IdAPI.thumbnail.concat(videoIds.get(i) + "/")
                        .concat(faces.getJSONObject(j).get("thumbnailId").toString()));
            }
            listLien.add(listUrlPhoto);
        }
        return listLien;
    }

}
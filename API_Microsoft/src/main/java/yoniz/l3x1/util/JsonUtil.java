package yoniz.l3x1.util;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.apiFace.IdAPI;
import yoniz.l3x1.videoIndexer.VideoIndexer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    /**
     * Lis un fichier texte qui contient les liens. Chaque ligne dans le fichier est un lien
     * @param pathTofile le chemin vers le fichier qui contient les liens
     * @return une liste se String qui contient les liens
     * @throws IOException
     */
    public static List<String> ReadFile (String pathTofile) throws IOException
    {
        String path;
        List<String> listeOfpaths = new ArrayList<String>();
        File file = new File(pathTofile);
        //teste si le fichier existe
        if (file.exists()) {
            // test si on peut lire le fichier
            if (file.canRead()) {
                BufferedReader buffer = new BufferedReader(new FileReader(file));
                while ((path = buffer.readLine()) != null) {
                    listeOfpaths.add(path);
                }
            }else {
                System.out.println("Le fichier ne peut pas être lu ");
            }
        }else {
            System.out.println("Le fichier n'existe pas ");
        }
        return listeOfpaths;
    }// END RedFile


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

    /*public static void jsonToFile (String json, String path)
    {
        String name = pathToName(path);
        try {
            PrintWriter pw = new PrintWriter(name);
            pw.println(json);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

    public static String pathToName(String path)
    {
        return path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
    }

    public static String supprimeGuillemet(String mot)
    {
        return mot.substring(1,mot.length()-1);
    }

    /*public static JsonObject jsonVideo (List<String> videoIds )
    {
        JsonObject jsonObject = Json.object();
        for (int i = 0; i<videoIds.size();i++)
        {
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

            jsonObject.add("photo"+String.valueOf(i), IdAPI.thumbnail.concat(videoIds.get(i) + "/")
                    .concat(faces.getJSONObject(j).get("thumbnailId").toString()));
        }

        JsonObject json1= Json.object().add("photo1","lien1").add("photo2","lien2");
        JsonObject json2 = Json.object().add("video1",json1);

    }*/

}
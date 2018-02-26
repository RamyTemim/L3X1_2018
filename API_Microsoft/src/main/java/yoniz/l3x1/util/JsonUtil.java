package yoniz.l3x1.util;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    /**
     * Lis un fichier texte qui contient les liens. Chaque ligne dans le fichier est un lien
     * @param pathTofile le chemain vers le fichier qui contient les liens
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

}
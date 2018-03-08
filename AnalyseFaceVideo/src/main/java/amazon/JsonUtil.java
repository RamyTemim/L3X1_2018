package amazon;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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



}

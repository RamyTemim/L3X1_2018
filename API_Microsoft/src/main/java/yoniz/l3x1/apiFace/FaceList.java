package yoniz.l3x1.apiFace;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import yoniz.l3x1.util.JsonUtil;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

public class FaceList {

    /**
     * Pour créer une liste (faceList) qui va pouvoir contenir une liste de photo
     * @param name Nom de la liste à créer
     * @param id L'identifiant de la liste qui sera utiliser plus tard pour rajouter des photos ou pour utiliser la méthode findSimilar
     * @param user Nom de l'utilisateur de la liste
     */
    public static void create(String name, String id, String user) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFaceList);

            builder.setPath(builder.getPath() + id);

            URI uri = builder.build();

            HttpPut request = new HttpPut(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            //Création du fichier Json pour l'envoyer à l'api
            JsonObject j= Json.object().add("name",name).add("userData",user);
            StringEntity reqEntity =new StringEntity(j.toString());


            request.setEntity(reqEntity);

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println("Création de la faceList " + id + " réussi \n");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * C'est une fonction qui va intéragir avec l'API et récuperer permettant de renvoyer l'ensembles des listes de photos contenus dans le compte de l'utilisateur
     * @return Renvois un Objet JSON contenant la liste des faceList qui correspond au retour de l'API de Microsoft
     */
    public static JSONObject getFaceList()
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject json = null;
        try {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFaceList);

            URI uri = builder.build();

            HttpGet request = new HttpGet(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println("Liste des FaceList : \n");
            json = JsonUtil.httpToJsonObject(entity);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return json;
    }

    //Pour obenir la liste des photos d'une face list
    public static void getFaceOflist(String id)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFaceList);

            builder.setPath(builder.getPath() + id);

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);


            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println("Liste des photos de la FaceList "+ id + ":");
            System.out.println(JsonUtil.httpToJsonObject(entity).toString(2));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    //Pour ajouter une photo à une face list
    public static void addFace(String path, String id, String userData)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFaceList);

            builder.setPath(builder.getPath() + id + "/persistedFaces");

            builder.addParameter("userData",userData);
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);


            File file = new File(path);

            FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(reqEntity);


            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public static void deleteFaceList(String id)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFaceList);

            builder.setPath(builder.getPath()+id);

            URI uri = builder.build();
            HttpDelete request = new HttpDelete(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);


            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}

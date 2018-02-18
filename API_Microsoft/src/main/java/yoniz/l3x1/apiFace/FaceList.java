package yoniz.l3x1.apiFace;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import yoniz.l3x1.util.Json;

import java.io.Closeable;
import java.io.File;
import java.net.URI;

public class FaceList {

    //Pour créer une face List
    public static void create(String name, String id, String user) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFaceList);

            builder.setPath(builder.getPath() + id);

            URI uri = builder.build();

            HttpPut request = new HttpPut(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);


            // Request body
            StringEntity reqEntity = new StringEntity(
                    "{\n" +
                            "    \"name\":\"" + name + "\",\n" +
                            "    \"userData\":\"" + user + "\"\n" +
                            "}");

            request.setEntity(reqEntity);

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                System.out.println(EntityUtils.toString(entity));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Pour afficher la liste des face List
    public static void getFaceList()
    {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFaceList);

            URI uri = builder.build();

            HttpGet request = new HttpGet(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            CloseableHttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            System.out.println("Liste des FaceList : \n");
            System.out.println(Json.httpToString(entity));

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

            System.out.println("Liste des Face de la FaceList "+ id+ ": \n");
            System.out.println(Json.httpToString(entity));
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
}

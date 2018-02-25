package yoniz.l3x1.videoIndexer;

import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import yoniz.l3x1.apiFace.IdAPI;
import yoniz.l3x1.util.JsonUtil;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

public class VideoIndexer {
    /**
     * Permet d'uploader la vidéo vers le cloud de microsoft pour qu'il l'analyse
     * @param path le chemin pour acceder à la vidéo
     * @return l'identifiant de la vidéo uploadé
     */
    public static String upload(String path)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //Pour le retourner en dehors du catch
        String videoId = null;

        try
        {
            //Transforme le lien en Uri
            URIBuilder builder = new URIBuilder(IdAPI.videoIndexerUpload);
            builder.toString();

            builder.addParameter("name", JsonUtil.pathToName(path));
            builder.addParameter("privacy", "Public");

            //builder.setPath(builder.toString().concat("name=").concat(JsonUtil.pathToName(path)).concat("&privacy=Public"));
            //builder.setPath(builder.getPath()+"name="+JsonUtil.pathToName(path)+"&privacy=Public");

            URI uri = builder.build();

            //Création de la requête HTTP Post
            HttpPost httpPost = new HttpPost(uri);
            //Header de la requête Post
            //httpPost.setHeader("Content-Type", "multipart/form-data");
            httpPost.setHeader("Ocp-Apim-Subscription-Key", IdAPI.videoKey);

            //Création de l'entite Multipart qui va être rajouté dans le http
            MultipartEntityBuilder multipart = MultipartEntityBuilder.create();

            //Rajout du fichier dans le multipart
            File f = new File(path);
            multipart.addBinaryBody("film2",new FileInputStream(f));

            //Transforme le multipart en entité Http
            HttpEntity entityMultipart = multipart.build();

            //Rajout de cette entité à la requête http POST
            httpPost.setEntity(entityMultipart);

            //Récupere la réponse de la requête Http
            CloseableHttpResponse response = httpclient.execute(httpPost);
            //Transforme la réponse en entité Http
            HttpEntity entity = response.getEntity();

            videoId = EntityUtils.toString(entity);

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return videoId;
    }

    public static JSONObject getBreakdown(String videoId)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject json = null;
        try
        {
            URIBuilder builder = new URIBuilder(IdAPI.videoIndexerUpload.concat(videoId));

            builder.setParameter("language", "French");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.videoKey);


            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            json = JsonUtil.httpToJsonObject(entity);

        }
            catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return json;
    }

    public static JSONArray getFacesFromVideos (JSONObject jsonObject)
    {
        return jsonObject.getJSONArray("breakdowns")
                .getJSONObject(0)
                .getJSONObject("insights")
                .getJSONArray("faces");

    }

    public static JSONObject getProcessingState (String videoId)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject json = null;
        try
        {
            URIBuilder builder = new URIBuilder(IdAPI.videoIndexerUpload.concat(videoId).concat("/State"));

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.videoKey);


            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            json = JsonUtil.httpToJsonObject(entity);

        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return json;
    }
}

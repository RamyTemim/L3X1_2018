package yoniz.l3x1.apiFace;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
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

public class DetectFace {

    //Pour demander une analyse d'une photo à l'API (Detect)
    public static HttpEntity requete (String path, String detectOnImage)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //Pour le retourner en dehors du catch
        HttpEntity entity=null;

        try {
            // Pour transformer le lien en URI (une URI c'est un String qui permet d'identifier une ressource du web)
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseDetect);

            // Pour définir les paramètres que le fichier json doit renvoyer
            // Request parameters. All of them are optional.
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            //builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");
            builder.setParameter("returnFaceAttributes", detectOnImage);

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            //request.setHeader("Content-Type", "application/json");
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            // Pour définir le lien vers la photo que l'on va intégrer dans la requête
            File file = new File(path);

            FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(reqEntity);


            // Pour effectuer l'appel vers l'API et recuperer le retour de cette appel
            CloseableHttpResponse response = httpclient.execute(request);
            entity = response.getEntity();

            return entity;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return entity;
    }

    //Pour demander à l'Api de renvoyer la
    public static JSONObject findSimilar(String faceListId, String faceId, String maxNbPersonne)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject jsonObject = null;
        try
        {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFindSimilar);

            URI uri = builder.build();

            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            JsonObject json = Json.object().add("faceId",faceId).add("faceListId",faceListId).add("maxNumOfCandidatesReturned",maxNbPersonne);

            StringEntity reqEntity = new StringEntity(json.toString());
            request.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            jsonObject = JsonUtil.httpToJsonObject(entity);
        }
        catch (Exception e)
        {
            //System.out.println(e.getMessage());
        }
        return jsonObject;
    }
}

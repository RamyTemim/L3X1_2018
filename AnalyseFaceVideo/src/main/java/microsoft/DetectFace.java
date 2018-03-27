package microsoft;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public class DetectFace {

    /**
     * C'est une fonction permettant d'envoyer la requête post à l'api de microsoft pour lui demander d'analyser une photo (Detect)
     * @param path Le chemin permettant d'accéder à la vidéo (en local si url = false ou en ligne si url = true)
     * @param detectOnImage Les éléments à détécter sur l'image (age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise)
     * @param url Un boolean permettant à la méthode de savoir si le path est une adresse local (si url = false) ou un url (si url = true)
     * @return Renvoit une un JSONObject qui correspond à l'objet Json renvoyé par l'API Face de microsoft
     */
    public static JSONObject detectFace (String path, String detectOnImage, Boolean url)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONObject jsonObject = null;

        try {
            // Pour transformer le lien en URI (une URI c'est un String qui permet d'identifier une ressource du web)
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseDetect);

            // Pour définir les paramètres que le fichier json doit renvoyer
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            builder.setParameter("returnFaceAttributes", detectOnImage);

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            insertFileToHttpRequest(path, url, request);

            // Pour effectuer l'appel vers l'API et recuperer le retour de cette appel
            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            jsonObject = JsonUtil.httpToJsonObject(entity);

        } catch (UnsupportedEncodingException e) {
            System.err.println("Erreur dans le format du StringEntity pour la méthode detectFace : " + e);
        } catch (ClientProtocolException e) {
            System.err.println("Erreur dans la requête HTTP pour la méthode detectFace : " + e);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier pour la méthode detectFace : " + e);
        } catch (URISyntaxException e) {
            System.err.println("Erreur lors du parse de l'URI  la méthode detectFace : " + e);
        }
        return jsonObject;
    }

    /**
     * Méthode pour insérer le fichier dans la requete HttpPost en fonction du boolean, si il est à true, c'est qu'il s'agit d'un url vers un site web
     * sinon il s'agit d'un lien vers un fichier stocké localement
     * @param path le chemin pour accéder au fichier
     * @param url le boolean permettant de savoir si c'est un url ou un fichier stocké localement
     * @param request la requête Post dans laquelle on va insérer le fichier
     */
    public static void insertFileToHttpRequest(String path, Boolean url, HttpPost request) throws UnsupportedEncodingException
    {
        if (url)
        {
            request.setHeader("Content-Type", "application/json");
            JSONObject json = new JSONObject().put("url", path);
            request.setEntity(new StringEntity(json.toString()));
        }

        else
        {
            request.setHeader("Content-Type", "application/octet-stream");

            // Pour définir le lien vers la photo que l'on va intégrer dans la requête
            File file = new File(path);
            FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(reqEntity);
        }
    }

    /**
     * Pour demander à l'API de renvoyer la (ou les) photos de la faceList qui correspondent à celle de la photo "modèle"
     * @param faceListId L'id de la faceList contenant les photos à comparer avec le modèle
     * @param faceId L'id de la photo qui sert de modèle pour la comparaison
     * @return Un fichier json contenant la photo que l'API à trouver qui correspond le plus au modèle
     */
    public static JSONArray findSimilarFace(String faceListId, String faceId)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONArray jsonArray = null;
        try {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFindSimilar);

            URI uri = builder.build();

            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            JSONObject json = new JSONObject().put("faceId", faceId)
                    .put("faceListId", faceListId);


            StringEntity reqEntity = new StringEntity(json.toString());
            request.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if(entity !=null)
                jsonArray = new JSONArray(EntityUtils.toString(entity).trim());

        } catch (JSONException e)
        {
            System.out.println("Aucune photo similaire n'a été trouvé ");
        }
        catch (UnsupportedEncodingException e) {
            System.err.println("Erreur dans le format du StringEntity pour la méthode findSimilarFace : " + e);
        } catch (ClientProtocolException e) {
            System.err.println("Erreur dans la requête HTTP pour la méthode findSimilarFace : " + e);
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier pour la méthode findSimilarFace : " + e);
        } catch (URISyntaxException e) {
            System.err.println("Erreur lors du parse de l'URI pour la méthode findSimilarFace : " + e);
        }

        if(jsonArray == null)
            return new JSONArray();

        return jsonArray;
    }
}


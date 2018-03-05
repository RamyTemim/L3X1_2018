package yoniz.l3x1.apiFace;

import org.apache.http.HttpEntity;
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
import org.json.JSONObject;
import yoniz.l3x1.util.JsonUtil;

import java.io.File;
import java.net.URI;

public class DetectFace {

    /**
     * C'est une fonction permettant d'envoyer la requête post à l'api de microsoft pour lui demander d'analyser une photo (Detect)
     * @param path Le chemin permettant d'accéder à la vidéo (en local si url = false ou en ligne si url = true)
     * @param detectOnImage Les éléments à détécter sur l'image (age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise)
     * @param url Un boolean permettant à la méthode de savoir si le path est une adresse local (si url = false) ou un url (si url = true)
     * @return Renvoit une un JSONObject qui correspond à l'objet Json renvoyé par l'API Face de microsoft
     */
    public static JSONObject detect (String path, String detectOnImage, Boolean url)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //Pour le retourner en dehors du catch
        JSONObject jsonObject = null;

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
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

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

            // Pour effectuer l'appel vers l'API et recuperer le retour de cette appel
            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            jsonObject = JsonUtil.httpToJsonObject(entity);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonObject;
    }

    /**
     * Pour demander à l'API de renvoyer la (ou les) photos de la faceList qui correspondent à celle de la photo "modèle"
     * @param faceListId L'id de la faceList contenant les photos à comparer avec le modèle
     * @param faceId L'id de la photo qui sert de modèle pour la comparaison
     * @return Un fichier json contenant la photo que l'API à trouver qui correspond le plus au modèle ainsi que l'indice de
     * confiance de l'API
     */
    public static JSONArray findSimilar(String faceListId, String faceId)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        JSONArray jsonArray = null;
        try
        {
            URIBuilder builder = new URIBuilder(IdAPI.uriBaseFindSimilar);

            URI uri = builder.build();

            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", IdAPI.subscriptionKey);

            JSONObject json = new JSONObject().put("faceId",faceId)
                                                .put("faceListId",faceListId);


            StringEntity reqEntity = new StringEntity(json.toString());
            request.setEntity(reqEntity);

            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            jsonArray = new JSONArray(EntityUtils.toString(entity).trim());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return jsonArray;
    }
}


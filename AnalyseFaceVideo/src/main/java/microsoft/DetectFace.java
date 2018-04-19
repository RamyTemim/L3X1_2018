package microsoft;

import identification.KeyMicrosoftApi;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import useful.Utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE microsoft
 * <p>
 * Cette classe contient les méthodes qui vont intéragir avec l'API Face
 * de microsoft pour tout ce qui est détéction de visage
 */
class DetectFace {

    private DetectFace() {
    }

    public static final Logger log = LogManager.getLogger();


    private static final String CONTENT = "Content-Type";

    /**
     * C'est une fonction permettant d'envoyer la requête post à l'api de microsoft pour lui demander d'analyser une photo (Detect)
     *
     * @param path Le chemin permettant d'accéder à la vidéo (en local si url = false ou en ligne si url = true)
     * @return Renvoit un JSONObject qui correspond à l'objet Json renvoyé par l'API Face de microsoft
     */
    static JSONObject detectFace(String path) {
        JSONObject jsonObject = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            try {
                // Pour transformer le lien en URI (une URI c'est un String qui permet d'identifier une ressource du web)
                URIBuilder builder = new URIBuilder(KeyMicrosoftApi.URI_BASE_DETECT);

                // Pour définir les paramètres que le fichier json doit renvoyer
                builder.setParameter("returnFaceId", "true");
                builder.setParameter("returnFaceLandmarks", "false");

                // Prepare the URI for the REST API call.
                URI uri = builder.build();
                HttpPost request = new HttpPost(uri);

                request.setHeader("Ocp-Apim-Subscription-Key", KeyMicrosoftApi.SUBSCRIPTION_KEY);

                insertFileToHttpRequest(path, request);

                // Pour effectuer l'appel vers l'API et recuperer le retour de cette appel
                CloseableHttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                jsonObject = Utils.httpToJsonObject(entity);

            } catch (UnsupportedEncodingException e) {
                log.info("Erreur dans le format du StringEntity pour la méthode detectFace : " + e);
            } catch (ClientProtocolException e) {
                log.info("Erreur dans la requête HTTP pour la méthode detectFace : " + e);
            } catch (IOException e) {
                log.info("Erreur lors de la lecture du fichier pour la méthode detectFace : " + e);
            } catch (URISyntaxException e) {
                log.info("Erreur lors du parse de l'URI  la méthode detectFace : " + e);
            }
        } catch (IOException e) {
            log.info("Erreur lors de la lecture du fichier dans la méthode detectFace : ");

        }
        return jsonObject;
    }

    /**
     * Méthode pour insérer le fichier dans la requete HttpPost en fonction du boolean, si il est à true,
     * c'est qu'il s'agit d'un url vers un site web
     * sinon il s'agit d'un lien vers un fichier stocké localement
     *
     * @param path    le chemin pour accéder au fichier
     * @param request la requête Post dans laquelle on va insérer le fichier
     */
    static void insertFileToHttpRequest(String path, HttpPost request) {

        request.setHeader(CONTENT, "application/octet-stream");

        // Pour définir le lien vers la photo que l'on va intégrer dans la requête
        File file = new File(path);
        FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
        request.setEntity(reqEntity);

    }

    /**
     * Pour demander à l'API de renvoyer la (ou les) photos de la faceList qui correspondent à celle de la photo "modèle"
     *
     * @param faceListId L'id de la faceList contenant les photos à comparer avec le modèle
     * @param faceId     L'id de la photo qui sert de modèle pour la comparaison
     * @return Un fichier json contenant la photo que l'API à trouver qui correspond le plus au modèle
     */
    static JSONArray findSimilarFace(String faceListId, String faceId) {


        JSONArray jsonArray = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try {
                URIBuilder builder = new URIBuilder(KeyMicrosoftApi.URI_BASE_FIND_SIMILAR);

                URI uri = builder.build();

                HttpPost request = new HttpPost(uri);
                request.setHeader(CONTENT, "application/json");
                request.setHeader("Ocp-Apim-Subscription-Key", KeyMicrosoftApi.SUBSCRIPTION_KEY);

                JSONObject json = new JSONObject().put("faceId", faceId)
                        .put("faceListId", faceListId);


                StringEntity reqEntity = new StringEntity(json.toString());
                request.setEntity(reqEntity);

                CloseableHttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                if (entity != null)
                    jsonArray = new JSONArray(EntityUtils.toString(entity).trim());

            } catch (JSONException e) {
                log.info("Aucune photo similaire n'a été trouvé ");
            } catch (UnsupportedEncodingException e) {
                log.info("Erreur dans le format du StringEntity pour la méthode findSimilarFace : " + e);
            } catch (ClientProtocolException e) {
                log.info("Erreur dans la requête HTTP pour la méthode findSimilarFace : " + e);
            } catch (IOException e) {
                log.info("Erreur lors de la lecture du fichier pour la méthode findSimilarFace : " + e);
            } catch (URISyntaxException e) {
                log.info("Erreur lors du parse de l'URI pour la méthode findSimilarFace : " + e);
            }
        } catch (IOException e) {
            log.info("Erreur lors de la lecture du fichier dans la méthode findSimilarFace : ");

        }

        if (jsonArray == null)
            return new JSONArray();

        return jsonArray;

    }

}


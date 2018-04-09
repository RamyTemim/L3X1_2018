package microsoft;

import identification.KeyMicrosoftApi;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import useful.JsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VideoIndexer {
    private VideoIndexer(){}

    private static final String OCP = "Ocp-Apim-Subscription-Key";

    /**
     * Permet d'uploader la vidéo vers le cloud de microsoft pour qu'il l'analyse
     * @param path le chemin pour acceder à la vidéo
     * @return l'identifiant de la vidéo uploadé
     */
    static String upload(String path) throws IOException {
        String videoId = null;
          try ( CloseableHttpClient httpclient = HttpClients.createDefault()) {

              //Pour le retourner en dehors du catch


              try {
                  //Transforme le lien en Uri
                  URIBuilder builder = new URIBuilder(KeyMicrosoftApi.VIDEO_INDEXER_UPLOAD);
                  builder.toString();

                  builder.addParameter("name", JsonUtil.pathToName(path));
                  builder.addParameter("privacy", "Public");

                  URI uri = builder.build();

                  //Création de la requête HTTP Post
                  HttpPost httpPost = new HttpPost(uri);
                  //Header de la requête Post

                  httpPost.setHeader(OCP, KeyMicrosoftApi.VIDEO_KEY);

                  //Création de l'entite Multipart qui va être rajouté dans le http
                  MultipartEntityBuilder multipart = MultipartEntityBuilder.create();

                  //Rajout du fichier dans le multipart
                  File f = new File(path);
                  multipart.addBinaryBody("film2", new FileInputStream(f));

                  //Transforme le multipart en entité Http
                  HttpEntity entityMultipart = multipart.build();

                  //Rajout de cette entité à la requête http POST
                  httpPost.setEntity(entityMultipart);

                  //Récupere la réponse de la requête Http
                  CloseableHttpResponse response = httpclient.execute(httpPost);
                  //Transforme la réponse en entité Http
                  HttpEntity entity = response.getEntity();

                  videoId = EntityUtils.toString(entity);

              } catch (FileNotFoundException e) {
                  JsonUtil.log.info("Erreur lors de la lecture du fichier pour la méthode upload : " + e);
              } catch (ClientProtocolException e) {
                  JsonUtil.log.info("Erreur dans la requête HTTP pour la méthode upload : " + e);
              } catch (IOException e) {
                  JsonUtil.log.info("Erreur lors de l'execution de la requete pour la méthode upload : " + e);
              } catch (URISyntaxException e) {
                  JsonUtil.log.info("Erreur lors du parse de l'URI  la méthode upload : " + e);
              }
          }
        return videoId;
    }

    /**
     * Méthode permettant de récuperer l'ensemble des informations qui on été extraite de la vidéo passé en paramètre par l'API Video Indexer
     * de Microsoft
     * @param videoId l'Id de la vidéo
     * @return Un objet Json qui va contenir l'ensemble des éléments que l'API de Microsoft a extrait de la vidéo
     */
    public static JSONObject getBreakdown(String videoId) throws IOException {
        JSONObject json = null;

    try(CloseableHttpClient httpclient = HttpClients.createDefault()) {

        try {
            URIBuilder builder = new URIBuilder(KeyMicrosoftApi.VIDEO_INDEXER_UPLOAD.concat(videoId));

            builder.setParameter("language", "French");

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            request.setHeader(OCP, KeyMicrosoftApi.VIDEO_KEY);


            CloseableHttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            json = JsonUtil.httpToJsonObject(entity);

        } catch (ClientProtocolException e) {
            JsonUtil.log.info("Erreur dans la requête HTTP pour la méthode geBreakdown : " + e);
        } catch (IOException e) {
            JsonUtil.log.info("Erreur lors de la lecture du fichier pour la méthode getBreakdown : " + e);
        } catch (URISyntaxException e) {
            JsonUtil.log.info("Erreur dans l'URI pour la méthode getBreakdown : " + e);
        }
    }
        return json;
    }

    /**
     * Méthode permettant de filtrer le Json récupéré par la méthode getBreakdown et de récuperer seulement la partie qui correspond aux
     * photos extraites de la vidéos
     * @param jsonObject l'objet JSON qui a été retourné par la méthode getBreakdown
     * @return Un Json contenant seulement la partie correspondant aux photos
     */
    public static JSONArray getFacesFromVideos (JSONObject jsonObject)
    {
        JSONArray json = jsonObject.getJSONArray("breakdowns")
                .getJSONObject(0)
                .getJSONObject("insights")
                .getJSONArray("faces");
        if(json !=null)
            return json;
        else
            return new JSONArray();
    }

    /**
     * Méthode permettant de récupérer l'état de la vidéo, il y a 4 états :
     * La vidéo s'est bien chargé dans leur cloud (Uploaded)
     * La vidéo ne s'est pas chargé (Failed)
     * La vidéo est en cours d'indéxation (Processing)
     * La vidéo est indéxé (Processed)
     * @param videoId L'id de la vidéo
     * @return renvoit l'objet JSON qui va contenir les informations à propos de la vidéo
     */
     static JSONObject getProcessingState (String videoId) throws IOException {
        JSONObject json = null;

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            try {
                URIBuilder builder = new URIBuilder(KeyMicrosoftApi.VIDEO_INDEXER_UPLOAD.concat(videoId).concat("/State"));

                URI uri = builder.build();

                final RequestConfig params = RequestConfig.custom().setConnectTimeout(3000).setSocketTimeout(3000).build();

                HttpGet request = new HttpGet(uri);
                request.setHeader(OCP, KeyMicrosoftApi.VIDEO_KEY);
                request.setConfig(params);
                CloseableHttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                json = JsonUtil.httpToJsonObject(entity);

            } catch (ClientProtocolException e) {
                JsonUtil.log.info("Erreur dans la requête HTTP pour la méthode getProcessingState : " + e);
            } catch (IOException e) {
                JsonUtil.log.info("Erreur lors de la lecture du fichier pour la méthode getProcessingState : " + e);
            } catch (URISyntaxException e) {
                JsonUtil.log.info("Erreur dans l'URI pour la méthode getProcessingState : " + e);
            }

            if (json == null)
                return new JSONObject();
            else
                return json;
        }
    }
}

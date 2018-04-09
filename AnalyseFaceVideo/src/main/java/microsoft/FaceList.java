package microsoft;

import identification.KeyMicrosoftApi;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;
import useful.JsonUtil;

import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.System.*;

class FaceList {
    private static final String  OCP = "Ocp-Apim-Subscription-Key";
    private static final String USER = "yoni";
    private static final Boolean URL = true;
    private  FaceList(){

    }


    /**
     * Pour cr&eacute;er une listePhoto (faceList) qui va pouvoir contenir une listePhoto de photo
     * @param name Nom de la listePhoto &agrave; cr&eacute;er
     * @param id L'identifiant de la listePhoto qui sera utiliser plus tard pour rajouter des photos ou pour utiliser la m&eacute;thode findSimilar
     */
    static void createFaceList(String name, String id) throws IOException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {

            try {
                URIBuilder builder = new URIBuilder(KeyMicrosoftApi.URI_BASE_FACE_LIST);

                builder.setPath(builder.getPath() + id);

                URI uri = builder.build();

                HttpPut request = new HttpPut(uri);
                request.setHeader("Content-Type", "application/json");
                request.setHeader(OCP, KeyMicrosoftApi.SUBSCRIPTION_KEY);

                //Création du fichier Json pour l'envoyer à l'api
                JSONObject j = new JSONObject().put("name", name).put("userData", USER);
                StringEntity reqEntity = new StringEntity(j.toString());

                request.setEntity(reqEntity);

                CloseableHttpResponse response = httpClient.execute(request);
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    JsonUtil.log.info("\nCréation de la faceList pour la vidéo " + (Integer.parseInt(id) + 1) + " réussi \n");
                }

            } catch (IOException | URISyntaxException e) {
                JsonUtil.log.info( e);
            }
        }
    }


    /**
     * Pour ajouter une photo dans une faceList (POST)
     * @param path Le chemin pour acc&eacute;der &agrave; la photo (en local)
     * @param id l'identifiant de la faceList dans laquelle il faut rajouter la photo
     * @param userData le label de la photo (nom de l'individu)
     */
    static void addFace(String path, String id, String userData) throws IOException {
       try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

           try {
               URIBuilder builder = new URIBuilder(KeyMicrosoftApi.URI_BASE_FACE_LIST);

               builder.setPath(builder.getPath() + id + "/persistedFaces");

               builder.addParameter("userData", userData);
               URI uri = builder.build();
               HttpPost request = new HttpPost(uri);
               request.setHeader(OCP, KeyMicrosoftApi.SUBSCRIPTION_KEY);

               DetectFace.insertFileToHttpRequest(path,URL, request);

               CloseableHttpResponse response = httpclient.execute(request);
               HttpEntity entity = response.getEntity();

               out.println(JsonUtil.httpToJsonObject(entity));

           } catch (IOException | URISyntaxException e) {
               JsonUtil.log.info( e);
           }

       }
    }


    /**
     * Pour renvoyer la listePhoto des photos d'une face list
     * @param id l'identifiant de la faceList &agrave; analyser
     * @return Le fichier JSON contenant la listePhoto des identifiant des photos de la faceList id
     */
    static JSONObject getFaceOflist(String id) throws IOException {
        JSONObject json = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            try {
                URIBuilder builder = new URIBuilder(KeyMicrosoftApi.URI_BASE_FACE_LIST);

                builder.setPath(builder.getPath() + id);

                URI uri = builder.build();
                HttpGet request = new HttpGet(uri);
                request.setHeader(OCP, KeyMicrosoftApi.SUBSCRIPTION_KEY);


                HttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();

                json = JsonUtil.httpToJsonObject(entity);
            } catch (IOException | URISyntaxException e) {
                JsonUtil.log.info(e);
            }
        }
        if (json == null)
            return new JSONObject();

        return json;
    }

    /**
     * Pour supprimer une faceList
     * @param id l'identifiant de la faceList à supprimer
     */
    static void deleteFaceList(String id) throws IOException, URISyntaxException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            URIBuilder builder = new URIBuilder(KeyMicrosoftApi.URI_BASE_FACE_LIST);

            builder.setPath(builder.getPath() + id);

            URI uri = builder.build();
            HttpDelete request = new HttpDelete(uri);
            request.setHeader(OCP, KeyMicrosoftApi.SUBSCRIPTION_KEY);


            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                out.println(EntityUtils.toString(entity));
            }
        }
    }
}

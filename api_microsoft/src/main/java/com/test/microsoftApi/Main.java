package com.test.microsoftApi;// This sample uses the Apache HTTP client library(org.apache.httpcomponents:httpclient:4.2.4)
// and the org.json library (org.json:json:20170516).

import java.io.File;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    public static final String subscriptionKey = "818c50f19c974fbc8601820ef5aaa75a";

    // Replace or verify the region.
    //
    // You must use the same region in your REST API call as you used to obtain your subscription keys.
    // For example, if you obtained your subscription keys from the westus region, replace
    // "westcentralus" in the URI below with "westus".
    //
    // NOTE: Free trial subscription keys are generated in the westcentralus region, so if you are using
    // a free trial subscription key, you should not need to change this region.
    public static final String uriBase = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";


    public static void main(String[] args) {
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Pour transformer le lien en URI (une URI c'est un String qui permet d'identifier une ressource du web)
            URIBuilder builder = new URIBuilder(uriBase);

            // Pour définir les paramètres que le fichier json doit renvoyer
            // Request parameters. All of them are optional.
            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            //builder.setParameter("returnFaceAttributes", "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise");
            builder.setParameter("returnFaceAttributes", "age,gender,smile,emotion,glasses");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            //request.setHeader("Content-Type", "application/json");
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);

            // Pour définir le lien vers la photo que l'on va intégrer dans la requête
            // Request body.
            //StringEntity reqEntity = new StringEntity("{\"url\":\"https://d34jodf30bmh8b.cloudfront.net/pictures/5661/5804/profile-1474295964-7c5694e2fc409f9ba430e094fee7f906.jpg\"}");
            File file = new File("/Users/yonizana/IdeaProjects/api_microsoft/src/main/resources/photo.jpg");

            FileEntity reqEntity = new FileEntity(file, ContentType.APPLICATION_OCTET_STREAM);
            request.setEntity(reqEntity);


            // Pour effectuer l'appel vers l'API et recuperer le retour de cette appel
            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();


            if (entity != null)
            {
                Json.JsonDisplay(EntityUtils.toString(entity).trim());
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }
}
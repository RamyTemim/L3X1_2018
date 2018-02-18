package yoniz.l3x1.videoIndexer;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

public class Main {

    public static void main(String[] args)
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        try
        {
            //Transforme le lien en Uri
            URIBuilder builder = new URIBuilder("https://videobreakdown.azure-api.net/Breakdowns/Api/Partner/Breakdowns?name=film2&privacy=Public");

            URI uri = builder.build();

            //Création de la requête HTTP Post
            HttpPost httpPost = new HttpPost(uri);
            //Header de la requête Post
            //httpPost.setHeader("Content-Type", "multipart/form-data");
            httpPost.setHeader("Ocp-Apim-Subscription-Key", "19b9d647b7e649b38ec9dbb472b6d668");

            //Création de l'entite Multipart qui va être rajouté dans le http
            MultipartEntityBuilder multipart = MultipartEntityBuilder.create();

            //Rajout du fichier dans le multipart
            File f = new File("src/main/resources/film2.mov");
            multipart.addBinaryBody("film2",new FileInputStream(f));

            //Transforme le multipart en entité Http
            HttpEntity entityMultipart = multipart.build();

            //Rajout de cette entité à la requête http POST
            httpPost.setEntity(entityMultipart);

            //Récupere la réponse de la requête Http
            CloseableHttpResponse response = httpclient.execute(httpPost);
            //Transforme la réponse en entité Http
            HttpEntity entity = response.getEntity();

            //Affiche le contenu de l'entité Http de la réponse
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




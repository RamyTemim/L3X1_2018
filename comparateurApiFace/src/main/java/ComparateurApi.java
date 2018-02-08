import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ComparateurApi {


    public static void main(String... argc) throws Exception{
        //création du client
        try(ImageAnnotatorClient client = ImageAnnotatorClient.create()){

            // varaible qui contien le chemain de l'image
            String ImagePath= "/home/hocine/IdeaProjects/comparateurApiFace/src/main/resources/257089-frederika.jpg";

            //charger le fichier en mémoire
            Path chemin = Paths.get(ImagePath);
            byte[] ImageData = Files.readAllBytes(chemin);
            ByteString ImageByte = ByteString.copyFrom(ImageData);

            //requette d'annotation
            List<AnnotateImageRequest> reponses = new ArrayList<>();
            Image img =Image.newBuilder().setContent(ImageByte).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest reponse = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            reponses.add(reponse);

            // dettection de l'etéquette
            BatchAnnotateImagesResponse request = client.batchAnnotateImages(reponses);
            List<AnnotateImageResponse> requests = request.getResponsesList();
            for (AnnotateImageResponse rsp :requests){
                if (rsp.hasError()){
                    System.out.printf( "Erreur!! %s\n",rsp.getError().getMessage() );
                    return;
                }
                for (EntityAnnotation ant:rsp.getLandmarkAnnotationsList()){
                    ant.getAllFields().forEach((k , v)->
                            System.out.printf("%s : %s \n", k, v.toString()));
                }

            }
        }
    }
}

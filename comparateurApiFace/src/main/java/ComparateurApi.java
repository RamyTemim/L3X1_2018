import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ComparateurApi {


    static  String  filepath="/home/hocine/L3X1_2018/comparateurApiFace/src/main/resources/image.jpg";

    public static void main(String[] argc) throws Exception,IOException{
        detectFaces(filepath, System.out);
        //parameter_procesing(argc,System.out);
    }// end main

   /* public static void parameter_procesing (String[]argc, PrintStream out) throws Exception, IOException{
         if(argc.length<1){
             out.println("Error!! --> use:  ");
             out.println("java -jar name_of_program path_to_picture");
             return;
         }
          // path to picture
         String command =argc[0];
         String path = argc[1];

        if (command.equals("faces")) {

                detectFaces(path, out);
            }// end if
        }// end paramater_processing */


    public static void detectFaces(String Path, PrintStream out) throws Exception, IOException {
        //List<AnnotateImageRequest> requests = new ArrayList<>();


        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            // The path to the image file to annotate
            // String fileName = "/home/hocine/IdeaProjects/FaceDetect/src/main/resources/axel_tabti.jpg";

            // Reads the image file into memory
            java.nio.file.Path path;
            path = Paths.get(filepath);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();
            requests.add(request);

            // Performs label detection on the image file
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            System.out.printf("%s : %s\n", k, v.toString()));
                }
            }
        }


    } // end detectFaces
}

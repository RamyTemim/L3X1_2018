import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ComparateurApi {


    public static void main(String[] argc) throws Exception {
        String filepath = "/home/hocine/L3X1_2018/comparateurApiFace/src/main/resources/tabti_hocine1.JPG";
        detectFaces(filepath, System.out);
        //parameter_procesing(argc,System.out);
    }// end main


    private static void detectFaces(String filepath, PrintStream out) throws Exception {
        //List<AnnotateImageRequest> requests = new ArrayList<>();


        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {


            // Reads the image file into memory

            Path path = Paths.get(filepath);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            // Builds the image annotation request
            List<AnnotateImageRequest> requests = new ArrayList<>();

            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
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

                for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
                    out.println("Landmark :" );
                    out.printf("",annotation.getBoundingPoly(),annotation.getLandmarksList());
                }
            }
        }


    } // end detectFaces
}//end class

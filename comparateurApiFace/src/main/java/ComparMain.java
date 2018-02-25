import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ComparMain {
    public  static  void main (String[] argc) throws IOException
    {
        String pathTo= "/home/hocine/L3X1_2018/comparateurApiFace/src/main/resources/tabti_hocine1.JPG";
        ComparateurApi service = new ComparateurApi(
                ComparateurApi.VERSION_DATE_2016_05_20
        );
        service.setApiKey("6da4215de53b99b208e7de9297b927fb5dcb26c8");

        InputStream imagesStream = new FileInputStream(pathTo);
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(imagesStream)
                .imagesFilename("tabti_hocine1.JPG")
                .build();
        ClassifiedImages result = service.classify(classifyOptions).execute();
        System.out.println(result);

    }//END MAIN
}// END CLASS

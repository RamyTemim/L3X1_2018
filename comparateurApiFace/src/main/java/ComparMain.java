import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ComparMain {
    public  static  void main (String[] argc) throws IOException
    {
        String pathTo= "";
        ComparateurApi service = new ComparateurApi(
                ComparateurApi.VERSION_DATE_2016_05_20
        );
        service.setApiKey("");

        InputStream imagesStream = new FileInputStream(pathTo);
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .imagesFile(imagesStream)
                .imagesFilename("")
                .build();
        ClassifiedImages result = service.classify(classifyOptions).execute();
        System.out.println(result);

    }//END MAIN
}// END CLASS

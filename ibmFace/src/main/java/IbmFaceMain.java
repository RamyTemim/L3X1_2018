import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IbmFaceMain {

    public static void main(String[] agrc) throws IOException {
          IbmFace service = new IbmFace(IbmFace.VERSION_DATE_2016_05_20);
          service.setApiKey("37b59d2fa25354d78f9e3afb8a67265b0db65f87");
          InputStream imagesStream = new FileInputStream("/home/hocine/L3X1_2018/ibmFace/src/main/resources/images.jpeg");
          ClassifyOptions classe = new ClassifyOptions.Builder().imagesFile(imagesStream).imagesFilename("images.jpeg").parameters("IBM").build();
          ClassifiedImages result =service.classify(classe).execute();
          System.out.println(result);

    }//END MAIN
}


import com.amazonaws.auth.AWSCredentials;
import org.json.JSONObject;

public class VideoDetect {

    public static       AWSCredentials credentials;

    public static void main(String[] args)  throws Exception
    {
        JSONObject resultOfAnalyse;
        resultOfAnalyse= ResultOnJson.getJsonObjectAmazon();
        System.out.println(resultOfAnalyse);

    }// END MAIN

}// END CLASS
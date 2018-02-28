import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.NotificationChannel;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;

public class VideoDetect {

  public static  AWSCredentials credentials;

    public static void main(String[] args)  throws Exception{
          AmazonSQS sqs = null;
          AmazonSNS sns = null;
          AmazonRekognition rek=null;
          String collectionId = "CollectionF";
          String bucket = "yanisaws";
          String nameOfImage = "V1.jpg";
          String startJobId = null;
          String queueUrl =  "https://sqs.us-east-1.amazonaws.com/027932523227/FileDattenteVideo";
          NotificationChannel channel= new NotificationChannel().withSNSTopicArn("arn:aws:sns:us-east-1:027932523227:analyse-video")
                .withRoleArn("arn:aws:iam::027932523227:role/Rekognition");
          String video ="yan.mov";

 //#########################################################################################################
 /*######*/CreatCollectionFaces.connexionIdexFace(credentials);                                     //######
 /*######*/CreatCollectionFaces.CreatCollectionFace(credentials, bucket, nameOfImage, collectionId);//######                                                                                   //######
 //#########################################################################################################


 //################################################################################################################################
 /*#####*/DetectFaceInVideo.connexionDetectFace(credentials, sqs, sns, rek);                                               //######
 /*#####*/DetectFaceInVideo.DetectFacesInVideos( bucket,  video,  startJobId,  rek,  channel, collectionId, queueUrl, sqs);//######
 //################################################################################################################################
    }
}
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.NotificationChannel;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

public class VideoDetect {



    public static void main(String[] args)  throws Exception{
          AmazonSQS sqs = null;
          AmazonSNS sns = null;
          AmazonRekognition rek=null;
          String collectionId = "CollectionF";
          String bucket = "yanisaws";
          String nameOfImage = "V1.jpg";
          String startJobId = null;
          String queueUrl =  "https://sqs.us-east-1.amazonaws.com/027932523227/FileDattenteVideo";
          NotificationChannel channel= new NotificationChannel().withSNSTopicArn("arn:aws:sns:us-east-1:027932523227:analyse-video").withRoleArn("arn:aws:iam::027932523227:role/Rekognition");
          String video ="yan.mov";
          AWSCredentials credentials;

 //#########################################################################################################
 /*######*/credentials =CreatCollectionFaces.connexionIdexFace();
           CreatCollectionFaces.DeleteCollection(collectionId,credentials);
 /*######*/CreatCollectionFaces.CreatCollectionFace(credentials, bucket, nameOfImage, collectionId);//######                                                                                   //######
 //#########################################################################################################

        sns = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        rek = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://rekognition.us-east-1.amazonaws.com", "us-east-1")).build();
 //################################################################################################################################
 /*#####*///DetectFaceInVideo.connexionDetectFace(credentials, sqs, sns, rek);                                               //######
 /*#####*/DetectFaceInVideo.DetectFacesInVideos( bucket,  video,  startJobId,  rek,  channel, collectionId, queueUrl, sqs);//######
 //################################################################################################################################
    }
}
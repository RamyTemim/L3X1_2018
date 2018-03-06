
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


import java.util.List;


public class VideoDetect {
  public static   String collectionId = "yoni";
  //public static  String video= "film1.mov";
  //public static String  bucketVideos = "yanisaws";
   // public static String  pho = "a.jpg";
    public static void main(String[] args)  throws Exception
    {


        String  bucketPhoto= "lxphoto";
        String  bucketVideo = "lxvideo";
       // String collectionId = "CollectionFac";
        String queueUrl =  "https://sqs.us-east-1.amazonaws.com/027932523227/FileDattenteVideo";
        NotificationChannel channel= new NotificationChannel().withSNSTopicArn("arn:aws:sns:us-east-1:027932523227:analyse-video").withRoleArn("arn:aws:iam::027932523227:role/Rekognition");

        AWSCredentials credentials;
        String  pathPhoto = "src/main/resources/listePhoto.txt";
        String  pathVideo = "src/main/resources/listeVideo.txt";

        List<String> listpathTophoto = JsonUtil.ReadFile(pathPhoto);
        List<String> listpathToVideo = JsonUtil.ReadFile(pathVideo);
/*
        S3operation.CreatBucket(bucketPhoto);
        for (String aListpathTophoto : listpathTophoto)
        {
            S3operation.UploadFileToBucket(bucketPhoto, aListpathTophoto);
        }

        S3operation.CreatBucket(bucketVideo);
        for (String aListpathToVideo : listpathToVideo)
        {
            S3operation.UploadFileToBucket(bucketVideo, aListpathToVideo);
        }*/


        credentials =CreatCollectionFaces.connexionIdexFace();
        CreatCollectionFaces.DeleteCollection(collectionId,credentials);
        CreatCollectionFaces.CreatCollectionFace(credentials, collectionId);

        List<String> listnameOfImage = S3operation.ListFilesInBucket(bucketPhoto);
        for (int i=0; i<listnameOfImage.size();i++)
        {
            CreatCollectionFaces.addFace(credentials, bucketPhoto, listnameOfImage.get(i) ,collectionId);
        }



        AmazonSNS  sns = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AmazonSQS  sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AmazonRekognition  rek = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://rekognition.us-east-1.amazonaws.com", "us-east-1")).build();


        List<String> listnameOfVideos = S3operation.ListFilesInBucket(bucketVideo);

        for (int i =0 ; i<listnameOfVideos.size();i++)
        {
            DetectFaceInVideo.DetectFacesInVideos(bucketVideo, listnameOfVideos.get(i), rek, channel, collectionId, queueUrl, sqs);
        }



        /*
        String collectionId = "CollectionF";
    String bucket = "yanisaws";
    String nameOfImage = "yanho.jpg";
    String queueUrl =  "https://sqs.us-east-1.amazonaws.com/027932523227/FileDattenteVideo";
    NotificationChannel channel= new NotificationChannel().withSNSTopicArn("arn:aws:sns:us-east-1:027932523227:analyse-video").withRoleArn("arn:aws:iam::027932523227:role/Rekognition");
    AWSCredentials credentials;



        credentials =CreatCollectionFaces.connexionIdexFace();
        CreatCollectionFaces.CreatCollectionFace(credentials, collectionId);
        CreatCollectionFaces.addFace(credentials,bucket,nameOfImage,collectionId);


        AmazonSNS  sns = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AmazonSQS  sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        AmazonRekognition  rek = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
                   .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://rekognition.us-east-1.amazonaws.com", "us-east-1")).build();
        DetectFaceInVideo.DetectFacesInVideos( bucket,  video,   rek,  channel, collectionId, queueUrl, sqs);
        CreatCollectionFaces.DeleteCollection(collectionId,credentials);

*/
}// END MAIN

}// END CLASS
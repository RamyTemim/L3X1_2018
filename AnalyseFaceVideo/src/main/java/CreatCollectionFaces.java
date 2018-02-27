import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sqs.AmazonSQS;


public class CreatCollectionFaces  {
    public static final String collectionId = "CollectionF";
    public static final String bucket= "yanisaws";
    public static String nameOfImage = "V1.jpg";
    private static AmazonSQS sqs = null;
    private static AmazonSNS sns = null;
    private static AmazonRekognition rek = null;
    private static NotificationChannel channel= new NotificationChannel().withSNSTopicArn("arn:aws:sns:us-east-1:027932523227:analyse-video")
            .withRoleArn("arn:aws:iam::027932523227:role/Rekognition");


    public static void connexion ()


   {    // données d'identifiction
       AWSCredentials credentials;
       // Connection au cloud d'amazon avec les données d'identification
       try {
           credentials = new ProfileCredentialsProvider("AdminUser").getCredentials();
       } catch (Exception e) {
           throw new AmazonClientException(
                   "Cannot load the credentials from the credential profiles file. " +
                           "Please make sure that your credentials file is at the correct " +
                           "location (/Users/userid/.aws/credentials), and is in valid format.", e);
       }

        // création une instance du service  Amazon Rekognition
       AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
               .standard()
               .withRegion(Regions.US_EAST_1)
               .withCredentials(new AWSStaticCredentialsProvider(credentials))
               .build();
        // création de collection avec collectionId = "CollectionF"
       CreateCollectionRequest request = new CreateCollectionRequest()
               .withCollectionId(collectionId);

       // chargement de l'image encoder en base64 en mémoire
       Image picture = new Image().withS3Object(new S3Object()
               .withBucket(bucket)
               .withName(nameOfImage));

       // Indexation du visage qui se trouve sur la photo
       IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
               .withImage(picture)
               .withCollectionId(collectionId)
               .withExternalImageId(nameOfImage)
               .withDetectionAttributes("ALL");

       /*
       IndexFacesResult indexFacesResult=amazonRekognition.indexFaces(indexFacesRequest);
       System.out.println(nameOfImage + " ajouter");
       List< FaceRecord > faceRecords = indexFacesResult.getFaceRecords();
       List<String> listOfFaceId = new ArrayList<String>();
       for (FaceRecord faceRecord: faceRecords) {
           listOfFaceId.add(faceRecord.getFace().getFaceId());
           System.out.println("Faceid est : " + faceRecord.getFace().getFaceId());
       }*/

   }

}

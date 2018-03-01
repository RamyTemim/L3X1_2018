import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;


import java.util.List;


public class CreatCollectionFaces {
    //public static final String collectionId = "CollectionF";
    //public static final String bucket = "yanisaws";
    //public static String nameOfImage = "V1.jpg";
    //private static AmazonSQS sqs = null;
    //private static AmazonSNS sns = null;
    //private static AmazonRekognition rek = null;
    //private static NotificationChannel channel = new NotificationChannel().withSNSTopicArn("arn:aws:sns:us-east-1:027932523227:analyse-video")
    //      .withRoleArn("arn:aws:iam::027932523227:role/Rekognition");


   public static AWSCredentials connexionIdexFace()

    {
        AmazonSQS sqs = null;
        AmazonSNS sns = null;
        AmazonRekognition rek=null;
        AWSCredentials credentials ;
        // Connection au cloud d'amazon avec les données d'identification
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "\t\t\t ############ CreatCollection #########\n"+
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (/Users/userid/.aws/credentials), and is in valid format.", e);
        }

        sns = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        rek = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://rekognition.us-east-1.amazonaws.com", "us-east-1")).build();

        return credentials;
    }
    public static AmazonRekognition getAWSR(AWSCredentials credentials)
    {
        // création une instance du service  Amazon Rekognition
        AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        return amazonRekognition;
    }


        public static void CreatCollectionFace (AWSCredentials credentials,String collectionId) {


            // création de collection avec collectionId = "CollectionF"
            CreateCollectionRequest request = new CreateCollectionRequest()
                    .withCollectionId(collectionId);

            CreateCollectionResult createCollectionResult = getAWSR(credentials).createCollection(request);
            System.out.println("Collection ARN = " + createCollectionResult.getCollectionArn());
        }// end Creat

    public static void addFace(AWSCredentials credentials,String bucket,String nameOfImage,String collectionId)
    {

        AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

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


       IndexFacesResult indexFacesResult=amazonRekognition.indexFaces(indexFacesRequest);

       System.out.println(nameOfImage + " ajouter");
       List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        for(FaceRecord faceRecord: faceRecords) {
                System.out.println("Face detected: Faceid is " +
                        faceRecord.getFace().getFaceId());

       }

    }// end ADDface

    public static void DeleteCollection (String collectionId,AWSCredentials credentials)
    {
        DeleteCollectionRequest request = new DeleteCollectionRequest()
                .withCollectionId(collectionId);
        DeleteCollectionResult deleteCollectionResult = getAWSR(credentials).deleteCollection(request);
        System.out.println("Suprimé ");
        System.out.println(collectionId + ": " + deleteCollectionResult.getStatusCode()
                .toString());

    }// end Delet

    }

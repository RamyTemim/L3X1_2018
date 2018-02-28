import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;

import java.util.ArrayList;
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

    /**
     * identéfication auprés du cloud d'amazon pour verifier les autorisation d'accé
     *
     * @param credentials fichier qui contien les clé d'accé
     */
    public static void connexionIdexFace(AWSCredentials credentials)

    {

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
    }


    /**
     * Création d'une collection pour stocker les métadonnées des visages
     * Puis index les visages qui se trouve sur la photo et les ajoute à la collection
     * @param credentials données d'identification
     * @param bucket l'endois ou est srtocker la photo sur le cloud S3
     * @param nameOfImage Le nom de l'image
     * @param collectionId l'identifiant de la collection
     */
        public static void CreatCollectionFace (AWSCredentials credentials,String bucket,String nameOfImage,String collectionId)
        {

        // création une instance du service  Amazon Rekognition
        AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
        // création de collection avec collectionId = "CollectionF"
        CreateCollectionRequest request = new CreateCollectionRequest()
                .withCollectionId(collectionId);

        CreateCollectionResult createCollectionResult = amazonRekognition.createCollection(request);
        System.out.println("Collection créer son nom est "+createCollectionResult.getCollectionArn());

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

    }

    }

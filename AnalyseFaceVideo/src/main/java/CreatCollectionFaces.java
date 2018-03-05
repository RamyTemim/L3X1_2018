import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;



import java.util.List;


public class CreatCollectionFaces {

    /**
     * Methode pour pour verifier les données d'identification et les autorisations  et les droits d'accé que l'utilisateur
     * @return credentials
     */
   public static AWSCredentials connexionIdexFace()
    {
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

        return credentials;
    }// END connexionIdexFace


    /**
     * Methode pour instancier  un client Amazon  dans la région pour lesquels l'utisateur est autorisé
     * @param credentials  données d'identification et autorisation d'accé
     *
     */
    private static AmazonRekognition getAWSR(AWSCredentials credentials)
    {
        return AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_2)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }


    /**
     * Méthode pour la création d'une collection qui vas contenir les méthadonnées des visage qui se trouve sur les photos
     * @param credentials données d'identification et autorisation d'accé
     * @param collectionId identifiant pour  la collection
     */
    public static void CreatCollectionFace (AWSCredentials credentials,String collectionId)
    {


            // création de collection avec collectionId = "CollectionF"
            CreateCollectionRequest request = new CreateCollectionRequest()
                    .withCollectionId(collectionId);

            CreateCollectionResult createCollectionResult = getAWSR(credentials).createCollection(request);
            System.out.println("Collection ARN = " + createCollectionResult.getCollectionArn());
        }// end Creat


    /**
     * Méthode pour analyser un visage et l'indxer et qui l'ajoute a la collection
     * @param credentials données d'identification et autorisation d'accé
     * @param bucket nom du comportément ou sont stocker les photos
     * @param nameOfImage nom de la photo qui contient le visage  à analyser
     * @param collectionId identifiant de la collection
     */
    public static void addFace(AWSCredentials credentials,String bucket,String nameOfImage,String collectionId)
    {

        AmazonRekognition amazonRekognition = AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_2)
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


    /**
     *  Méthode pour supprimer une collecion
     * @param collectionId identifiant de la collection a supprimer
     * @param credentials  données d'identification et autorisation d'accé
     */
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

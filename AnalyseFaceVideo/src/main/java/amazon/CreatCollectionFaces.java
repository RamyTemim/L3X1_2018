/*
 * L3X1 FACIAL RECONGITION COMPARATOR
 *
 * IA as a service (Facial recognition on vidéo)
 *
 * PACKAGE AMAZON
 *
 *
 * Cette classe contient des méthodes qui seront utiliser pour
 * interagir avec les service AWS: vérifier l'identité de l'utilisateur
 * et ces autorisations, manipuler les photos de
 * profiles qui seront donner comme input par l'utilisateur.
 *
 * Cette classe est en interaction avec la classe AmazonService
 * qui se trouve dans le package service du package springboot.
 *
 * La classe AmazonSerive va faire appel au méthodes :
 *
 *  - creatCollectionFace() : pour créer une collection vide
 *  - addFace() : ajouter les visages détecté sur les photos dans la collection
 *  - deleteCollection() : supprimer la collection a la fin de la recherche
 *
 */
package amazon;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import useful.JsonUtil;

import java.util.List;




public class CreatCollectionFaces {

     private CreatCollectionFaces(){ }

    /**
     * Méthode pour vérifier les données d'identifications, les autorisations et les
     * * droits d’accès de l'utilisateur qui se trouve dans le fichier credentials
     * * @return credentials vérifier par Amazon
     */
   public static AWSCredentials connexionIdexFace()
    {
        AWSCredentials credentials ;
        // Connexion au cloud d'amazon avec les données d'identifications
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
    }


    /**
     * Méthode pour instancier un client Amazon dans la région pour laquelle
     * le credentials a été vérifier. le client sera utiliser pour interagir
     * avec les service d'AWS
     * @param credentials données d'identification et autorisation d'accès
     */
    private static AmazonRekognition getAWSR(AWSCredentials credentials)
    {
        return AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }


    /**
     * Méthode pour la création d'un objet collection qui va contenir les méta-données des
     * visages qui se trouvent sur les photos de profiles pour les comparer ensuite au visages
     * qui se trouve sur les vidéos
     * @param credentials données d'identification et autorisation d'accé
     * @param collectionId identifiant pour  la collection
     */
    public static void creatCollectionFace (AWSCredentials credentials,String collectionId)
    {
            // création de collection avec collectionId = "CollectionF"
            CreateCollectionRequest request = new CreateCollectionRequest()
                    .withCollectionId(collectionId);

            CreateCollectionResult createCollectionResult = getAWSR(credentials).createCollection(request);
        JsonUtil.log.info("Collection ARN = " + createCollectionResult.getCollectionArn());
        }


    /**
     * Méthode pour analyser un visage, l'indexer et recueillir les méta-données
     * puis l'ajouter à la collection créer avec la méthode creatCollectionFace()
     * @param credentials données d'identification et autorisation d'accès
     * @param bucket nom du comportiment où sont stockés les photos
     * @param nameOfImage nom de la photo qui contient le visage à analyser
     * @param collectionId identifiant de la collection
     */
    public static void addFace(AWSCredentials credentials,String bucket,String nameOfImage,String collectionId)
    {
       // chargement de l'image encoder en base64 en mémoire
        Image picture = new Image().withS3Object(new S3Object().withBucket(bucket).withName(nameOfImage));

        // Indexation du visage qui se trouve sur la photo
        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(picture)
                .withCollectionId(collectionId)
                .withExternalImageId(nameOfImage)
                .withDetectionAttributes("ALL");


       IndexFacesResult indexFacesResult=getAWSR(credentials).indexFaces(indexFacesRequest);

        JsonUtil.log.info(nameOfImage + " ajouter");
       List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        for(FaceRecord faceRecord: faceRecords) {
            JsonUtil.log.info("Face detected: Faceid is " +
                        faceRecord.getFace().getFaceId());

       }

    }


    /**
     * Méthode pour supprimer une collecion
     * @param collectionId identifiant de la collection a supprimer
     * @param credentials  données d'identification et autorisation d'accés
     */
    public static void deleteCollection (String collectionId,AWSCredentials credentials)
    {
        DeleteCollectionRequest request = new DeleteCollectionRequest()
                .withCollectionId(collectionId);
        DeleteCollectionResult deleteCollectionResult = getAWSR(credentials).deleteCollection(request);
        JsonUtil.log.info("Suprimé ");
        JsonUtil.log.info(collectionId + ": " + deleteCollectionResult.getStatusCode()
                .toString());

    }

}

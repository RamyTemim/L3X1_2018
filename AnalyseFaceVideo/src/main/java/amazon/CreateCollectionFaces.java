package amazon;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE amazon
 * <p>
 * Cette classe contient des méthodes qui seront utiliser pour
 * interagir avec les service AWS: vérifier l'identité de l'utilisateur
 * et ces autorisations, manipuler les photos de
 * profiles qui seront donner comme input par l'utilisateur.
 * <p>
 * Cette classe est en interaction avec la classe AmazonService
 * qui se trouve dans le package service du package springboot.
 * <p>
 * La classe AmazonSerive va faire appel au méthodes :
 * <p>
 * - createCollectionFace() : pour créer une collection vide
 * - addFace() : ajouter les visages détecté sur les photos dans la collection
 * - deleteCollection() : supprimer la collection a la fin de la recherche
 */
public class CreateCollectionFaces {

    private CreateCollectionFaces() {
    }

    public static final Logger log = LogManager.getLogger();

    /**
     * Méthode pour vérifier les données d'identifications, les autorisations et les
     * droits d’accès de l'utilisateur qui se trouve dans le fichier credentials
     *
     * @return credentials vérifié par Amazon
     */
    public static AWSCredentials connexionIndexFace() {
        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Le credentials n'a pas été trouvé dasn votre répertoir "+ e);
        }

        return credentials;
    }


    /**
     * Méthode pour instancier un client Amazon dans la région pour laquelle
     * le credentials a été vérifier.
     * Le client sera utiliser pour interagir avec les service d'AWS
     *
     * @param credentials données d'identification et autorisation d'accès
     */
    private static AmazonRekognition getAWSR(AWSCredentials credentials) {
        return AmazonRekognitionClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }


    /**
     * Méthode pour la création d'un objet collection qui va contenir les méta-données des
     * visages qui se trouvent sur les photos de profils pour les comparer ensuite aux visages
     * qui se trouvent sur les vidéos
     *
     * @param credentials  données d'identification et autorisation d'accés
     * @param collectionId identifiant pour la collection
     */
    public static void createCollectionFace(AWSCredentials credentials, String collectionId) {
        // création de collection avec collectionId = "CollectionF"
        CreateCollectionRequest request = new CreateCollectionRequest()
                .withCollectionId(collectionId);

        CreateCollectionResult createCollectionResult = getAWSR(credentials).createCollection(request);
        log.info("Collection ARN = " + createCollectionResult.getCollectionArn());
    }


    /**
     * Méthode pour analyser un visage, l'indexer et recueillir les méta-données
     * puis l'ajouter à la collection créé avec la méthode createCollectionFace()
     *
     * @param credentials  données d'identification et autorisation d'accès
     * @param bucket       nom du comportiment où sont stockés les photos
     * @param nameOfImage  nom de la photo qui contient le visage à analyser
     * @param collectionId identifiant de la collection
     */
    public static void addFace(AWSCredentials credentials, String bucket, String nameOfImage, String collectionId) {
        // chargement de l'image encoder en base64 en mémoire
        Image picture = new Image().withS3Object(new S3Object().withBucket(bucket).withName(nameOfImage));

        // Indexation du visage qui se trouve sur la photo
        IndexFacesRequest indexFacesRequest = new IndexFacesRequest()
                .withImage(picture)
                .withCollectionId(collectionId)
                .withExternalImageId(nameOfImage)
                .withDetectionAttributes("ALL");


        IndexFacesResult indexFacesResult = getAWSR(credentials).indexFaces(indexFacesRequest);

        log.info(nameOfImage + " ajouté");
        List<FaceRecord> faceRecords = indexFacesResult.getFaceRecords();
        for (FaceRecord faceRecord : faceRecords) {
            log.info("Face detected: Faceid is " +
                    faceRecord.getFace().getFaceId());

        }

    }


    /**
     * Méthode pour supprimer une collecion
     *
     * @param collectionId identifiant de la collection a supprimer
     * @param credentials  données d'identification et autorisation d'accès
     */
    public static void deleteCollection(String collectionId, AWSCredentials credentials) {
        DeleteCollectionRequest request = new DeleteCollectionRequest()
                .withCollectionId(collectionId);
        DeleteCollectionResult deleteCollectionResult = getAWSR(credentials).deleteCollection(request);
        log.info("Suprimé ");
        log.info(collectionId + ": " + deleteCollectionResult.getStatusCode()
                .toString());

    }

}

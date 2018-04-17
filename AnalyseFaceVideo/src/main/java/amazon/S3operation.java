package amazon;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import useful.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE amazon
 * <p>
 * Cette classe contient des méthodes qui seront utiliser
 * pour interagir avec les service S3 d'AWS qui correspond au cloud
 * de storage d'amazon.
 * <p>
 * Elle contient des méthodes pour s'authentifier auprès du
 * service S3, et des methodes pour manipuler les compartiment
 * de stockage.
 * <p>
 * Cette classe est en interaction avec la classe AmazonService
 * qui se trouve dans le package service du package springboot.
 * <p>
 * La classe AmazonService va faire appel au méthodes :
 * - createBucket() : créer un compartiment de stockage
 * - uploadFileToBucket() : envoyer les fichiers données comme input dans le cloud
 * - listFilesInBucket(): récupérer les noms des fichiers stockés
 * - purgeBucket(): vider le compartiment pour ne pas garder les
 * photos et videos des utilisateurs
 */
public class S3operation {

    private S3operation() {
    }

    public static final Logger log = LogManager.getLogger();

    private static Upload upload;

    /**
     * Méthode pour créer un client S3 pour utiliser le service S3
     * et manipuler les compartiment qui se trouvent sur le cloud d'Amazon
     *
     * @return s3client un client S3 pour utiliser le service AWS S3
     */
    private static AmazonS3 getS3Client() {
        AmazonS3 s3client = new AmazonS3Client();
        s3client.setRegion(Region.getRegion(Regions.US_EAST_1));

        return s3client;
    }// END  getS3Client


    /**
     * Méthode  pour créer un compartiment dans le service S3 pour y stocker
     * les photos et les vidéos afin de les analyser
     *
     * @param bucketName le nom que vous voulez donner au compartiment
     */
    public static void creatBucket(String bucketName) {
        try {
            if (!(getS3Client().doesBucketExistV2(bucketName))) {
                CreateBucketRequest bucket = new CreateBucketRequest(bucketName);
                getS3Client().createBucket(bucket);
            }

        } catch (AmazonServiceException ase) {
            log.info("La création du compartiment n'a pa pu etre effectuer a cause client  : " + ase.getMessage());


        } catch (AmazonClientException ace) {
            log.info("La création du compartiment n'a pa pu etre effectuer a cause : ");
            log.info("Error Message: " + ace.getMessage());
        }

    }// END  CreatBucket


    /**
     * Methode pour uploader un fichier dans un compartiment S3
     * et stocker les photos et vidéos dedans
     *
     * @param bucketName le nom du compartiment dans lequel vous voulez uploader le fichier
     * @param filePath   le chemin relative du fichier que vous voulez uploader
     */
    public static void uploadFileToBucket(String bucketName, String filePath) {

        try {
            File file = new File(filePath);
            String keyName = Utils.pathToName(filePath);
            TransferManager tm = new TransferManager(new ProfileCredentialsProvider());
            // TransferManager processes all transfers asynchronously,
            // so this call will return immediately.
            Upload upload = tm.upload(bucketName, keyName, file);
            try {
                // Or you can block and wait for the upload to finish
                upload.waitForCompletion();
                System.out.println("Chargement réussi");
            } catch (AmazonClientException amazonClientException) {
                System.out.println("Impossible de charger le fichier le chargement a étais annuler .");
                amazonClientException.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println(" le fichier n'a pas pu être lu ");
        }
    }// END UploadFileToBucket

    /**
     * Méthode pour attendre le chargement du fichier
     */
    private static void uploadWait() {
        try {

            upload.waitForCompletion();
            log.info("Chargement réussi");
        } catch (AmazonClientException amazonClientException) {
            log.info("Impossible de charger le fichier le chargement a été annuler .");
            log.info(amazonClientException);
        } catch (InterruptedException e) {
            log.info("Erreur dans la méthode uploadWait : " + e);
        }

    }

    /**
     * Méthode pour récupérer la listePhoto des fichiers qui se trouve dans un compartiment
     *
     * @param bucketName le nom du compartiment duquel vous voulez récupérer les fichiers
     * @return listePhoto de string qui contient les noms des fichiers
     */

    public static List<String> listFilesInBucket(String bucketName) {
        ObjectListing objectListing = getS3Client().listObjects(new ListObjectsRequest().withBucketName(bucketName));
        List<S3ObjectSummary> summary = objectListing.getObjectSummaries();
        List<String> listefile = new ArrayList<>();
        for (S3ObjectSummary sum : summary) {
            listefile.add(sum.getKey());
        }
        return listefile;

    }// END  ListOfFiles


    /**
     * Méthode qui vide un compartiment S3
     * a la fin de l'analyse et de la recherche
     *
     * @param bucketName le nom du compartiment à vider
     */
    public static void purgeBucket(String bucketName) {

        try {
            ObjectListing objectListing = getS3Client().listObjects(bucketName);
            while (true) {
                for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                    getS3Client().deleteObject(bucketName, summary.getKey());
                }
                if (objectListing.isTruncated()) {
                    objectListing = getS3Client().listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
        } catch (AmazonServiceException e) {
            log.info(e);
            exit(1);
        }

    }// END  PurgeBucket

}//END CLASs
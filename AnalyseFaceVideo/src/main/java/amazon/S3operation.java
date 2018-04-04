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
import microsoft.JsonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.*;

public class S3operation {

    private S3operation(){}
    private static  Logger log =LogManager.getLogger();

    /**
     * Méthode pour créer un client S3
     * @return s3client un clien S3 pour utilise le service AWS S3
     */
    private static AmazonS3 getS3Client() {
        AmazonS3 s3client = new AmazonS3Client();
        s3client.setRegion(Region.getRegion(Regions.US_EAST_1));

        return s3client;
    }// END  getS3Client


    /**
     * Méthode  pour créer un compartiment dans le service S3 pour y stocker les photos et les videos
     * @param bucketName le nom que vous voulez donner au compartiment
     */
    public static void creatBucket(String bucketName)
    {
        try {
            if (!(getS3Client().doesBucketExist(bucketName))) {
                CreateBucketRequest bucket= new CreateBucketRequest(bucketName);
                getS3Client().createBucket(bucket);
            }

        } catch (AmazonServiceException ase) {
            log.info("La création du compartiment n'a pa pu etre effectuer a cause : ");
            log.info("Error Message:    " + ase.getMessage());
            log.info("HTTP Status Code: " + ase.getStatusCode());
            log.info("AWS Error Code:   " + ase.getErrorCode());
            log.info("Error Type:       " + ase.getErrorType());
            log.info("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            log.info("La création du compartiment n'a pa pu etre effectuer a cause :.");
            log.info("Error Message: " + ace.getMessage());
        }

    }// END  CreatBucket


    /**
     * Methode pour uploader un fichier dans un compartiment S3
     * @param bucketName le nom du compartiment dans lequel vous voulez uploader le fichier
     * @param filePath le chemain relative du fichier que vous voulez uploader
     *
     */

    public static void uploadFileToBucket(String bucketName,String filePath)  {

        try
        {
            File file = new File(filePath);

            String keyName = JsonUtil.pathToName(filePath);
            TransferManager tm = new TransferManager(new ProfileCredentialsProvider());
            Upload upload = tm.upload(bucketName, keyName , file );

            try {

                upload.waitForCompletion();
                out.println("Chargement réussi");
            } catch (AmazonClientException amazonClientException) {
                log.info("Impossible de charger le fichier le chargement a étais annuler .");
                log.info(amazonClientException);
            }
        } catch (Exception e)
        {
            log.info(e);
        }
    }// END UploadFileToBucket

    /**
     * Méthode pour récupérer la listePhoto des fichiers qui se trouve dans un compartiment
     * @param bucketName le nom du compartiment du quel vous voulez récupérer les fichiers
     * @return listePhoto de srting qui contient les nom des fichiers
     */

    public  static List<String>  listFilesInBucket(String bucketName)
    {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3client.setRegion(Region.getRegion(Regions.US_EAST_1));
        ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        List<S3ObjectSummary> summary =  objectListing.getObjectSummaries();
        List<String> listefile =new ArrayList<>();
        for (S3ObjectSummary sun : summary)
        {
            listefile.add(sun.getKey());
        }
     return listefile;

    }// END  ListOfFiles


    /**
     * Méthode qui vide un compartiment S3
     * @param bucketName le nom du compartiment a vider
     */
    public  static  void  purgeBucket (String bucketName)
    {

        try {
            ObjectListing objectListing = getS3Client().listObjects(bucketName);
            while (true) {
                for (S3ObjectSummary summary : objectListing.getObjectSummaries()) {
                    getS3Client().deleteObject(bucketName, summary.getKey());
                }

                // more object_listing to retrieve?
                if (objectListing.isTruncated()) {
                    objectListing = getS3Client().listNextBatchOfObjects(objectListing);
                } else {
                    break;
                }
            }
        }catch (AmazonServiceException e)
        {
            log.info(e);
            exit(1);
        }

    }// END  PurgeBucket

}//END CLASs
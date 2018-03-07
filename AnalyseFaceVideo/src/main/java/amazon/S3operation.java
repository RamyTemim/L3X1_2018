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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class S3operation {

    private S3operation(){}


    /**
     * Méthode pour créer un client S3
     * @return s3client un client identifier
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
    public static void CreatBucket(String bucketName)
    {
        try {
            if (!(getS3Client().doesBucketExist(bucketName))) {
                CreateBucketRequest bucket= new CreateBucketRequest(bucketName);
                getS3Client().createBucket(bucket);
            }

        } catch (AmazonServiceException ase) {
            System.out.println("La création du compartiment n'a pa pu etre effectuer a cause : ");
            System.err.println("Error Message:    " + ase.getMessage());
            System.err.println("HTTP Status Code: " + ase.getStatusCode());
            System.err.println("AWS Error Code:   " + ase.getErrorCode());
            System.err.println("Error Type:       " + ase.getErrorType());
            System.err.println("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            System.err.println("La création du compartiment n'a pa pu etre effectuer a cause :.");
            System.err.println("Error Message: " + ace.getMessage());
        }
    }// END  CreatBucket


    /**
     * Methode pour uploader un fichier dans un compartiment S3
     * @param bucketName le nom du compartiment dans lequel vous voulez uploader le fichier
     * @param filePath le chemain relative du fichier que vous voulez uploader
     *
     */
    public static void UploadFileToBucket(String bucketName,String filePath)
    {

       try
       {
           File file = new File(filePath);

        String keyName = file.getName();
        TransferManager tm = new TransferManager(new ProfileCredentialsProvider());
        // TransferManager processes all transfers asynchronously,
        // so this call will return immediately.
        Upload upload = tm.upload(bucketName, keyName , file );

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
       } catch (Exception e)
       {
           System.err.println(" le fichier n'a pas pu être lu ");
       }
    }// END UploadFileToBucket


    /**
     * Méthode pour récupérer la liste des fichiers qui se trouve dans un compartiment
     * @param bucketName le nom du compartiment du quel vous voulez récupérer les fichiers
     * @return liste de srting qui contient les nom des fichiers
     */
    public  static List<String>  ListFilesInBucket(String bucketName)
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
    public  static  void  PurgeBucket (String bucketName)
    {

        try {
            ObjectListing object_listing = getS3Client().listObjects(bucketName);
            while (true) {
                for (S3ObjectSummary summary : object_listing.getObjectSummaries()) {
                    getS3Client().deleteObject(bucketName, summary.getKey());
                }

                // more object_listing to retrieve?
                if (object_listing.isTruncated()) {
                    object_listing = getS3Client().listNextBatchOfObjects(object_listing);
                } else {
                    break;
                }
            }
        }catch (AmazonServiceException e)
        {
            System.out.println(e.getErrorMessage());
            System.exit(1);
        }

    }// END  PurgeBucket

}//END CLASs
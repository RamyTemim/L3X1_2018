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
import java.util.Iterator;
import java.util.List;


public class S3operation {

    private static String bucketName = "";
    String existingBucketName = "*** Provide existing bucket name ***";
    String keyName            = "*** Provide object key ***";
    String filePath           = "*** Path to and name of the file to upload ***";


    private static AmazonS3 getS3Client() {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3client.setRegion(Region.getRegion(Regions.US_EAST_1));

        return s3client;
    }// END  getS3Client


    public static String CreatBucket(String bucketName)
    {
        try {
            if (!(getS3Client().doesBucketExist(bucketName))) {

                getS3Client().createBucket(new CreateBucketRequest(bucketName));
            }

        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
        return bucketName;
    }// END  CreatBucket



    public static void UploadFileToBucket( String existingBucketName, String keyName,  String filePath, String bucketName) throws Exception
    {

        File file = new File(filePath);
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
        }

    }// END UploadFileToBucket


    public  static List<String>  ListFilesInBucket(String bucketName)
    {
        AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider());
        s3client.setRegion(Region.getRegion(Regions.US_EAST_1));
        ObjectListing objectListing = s3client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
        List<S3ObjectSummary> summary =  objectListing.getObjectSummaries();
        List<String> listefile =new ArrayList<String>();
        for (S3ObjectSummary sun : summary)
        {
            listefile.add(sun.getKey());
        }
     return listefile;

    }// END  ListOfFiles


    public  static  void  PurgeBucket (String bucketName)
    {

        try {
            System.out.println(" - removing objects from bucket");
            ObjectListing object_listing = getS3Client().listObjects(bucketName);
            while (true) {
                for (Iterator<?> iterator =
                     object_listing.getObjectSummaries().iterator();
                     iterator.hasNext(); ) {
                    S3ObjectSummary summary = (S3ObjectSummary) iterator.next();
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
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }

    }// END  PurgeBucket

}//END CLASs



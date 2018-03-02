
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class DetectFaceInVideo {

  private static   String startJobId=null;
/*
    public static AWSCredentials connexionDetectFace( AmazonSQS sqs, AmazonSNS sns, AmazonRekognition rek)

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
        // Connection au cloud d'amazon avec les données d'identification

        sns = AmazonSNSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
        rek = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://rekognition.us-east-1.amazonaws.com", "us-east-1")).build();

        return credentials;
    }// end connexion
*/

    public static  void DetectFacesInVideos(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId,String queueUrl,AmazonSQS sqs) throws Exception
    {
        //String startJobId = "a849a056ed1ec63561d1bcab7a52c049b6570cc2fa0094adc5470b12469d2849";
        //##################################################################################
        StartFaceSearchCollection(bucket,video, rek,  channel, collectionId);//##
        //##################################################################################

        System.out.println("Waiting for job: " + startJobId);
        //Poll queue for messages
        List<Message> messages=null;
        int dotLine=0;
        boolean jobFound=false;
        //loop until the job status is published. Ignore other messages in queue.
        do
            {
            //Get messages.
            do
                {
                messages = sqs.receiveMessage(queueUrl).getMessages();
                if (dotLine++<20)
                {
                    System.out.print(".");
                }else
                    {
                    System.out.println();
                    dotLine=0;
                }
            }while(messages.isEmpty());

            System.out.println();
            //Loop through messages received.
            for (Message message: messages)
            {
                String notification = message.getBody();
                // Get status and job id from notification.
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonMessageTree = mapper.readTree(notification);
                JsonNode messageBodyText = jsonMessageTree.get("Message");
                ObjectMapper operationResultMapper = new ObjectMapper();
                JsonNode jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
                JsonNode operationJobId = jsonResultTree.get("JobId");
                JsonNode operationStatus = jsonResultTree.get("Status");
                System.out.println("Job found was " + operationJobId);
                // Found job. Get the results and display.
                if(operationJobId.asText().equals(startJobId))
                {
                    jobFound=true;
                    System.out.println("Job id: " + operationJobId );
                    System.out.println("Status : " + operationStatus.toString());
                    if (operationStatus.asText().equals("SUCCEEDED"))
                    {
                        //#################################################
                         GetResultsFaceSearchCollection(startJobId,rek);//##
                        //============================================
                    }
                    else
                        {
                        System.out.println("Video analysis failed");
                    }
                    sqs.deleteMessage(queueUrl,message.getReceiptHandle());
                }
                else
                    {
                    System.out.println("Job received was not job " +  startJobId);
                }
            }
        } while (!jobFound);

        System.out.println("Done!");
    }// end DetectFacesInVideos

    private static void StartFaceSearchCollection(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId)
    {
        StartFaceSearchRequest req = new StartFaceSearchRequest()
                .withCollectionId(collectionId)
                .withVideo(new Video().withS3Object(new S3Object().withBucket(bucket).withName(video)))
                .withNotificationChannel(channel);

        StartFaceSearchResult startPersonCollectionSearchResult = rek.startFaceSearch(req);
        startJobId = startPersonCollectionSearchResult.getJobId();


    }// END Start


    private static void   GetResultsFaceSearchCollection(String startJobId, AmazonRekognition rek)
    {

        GetFaceSearchResult faceSearchResult=null;
     // int maxResults=10;
       String paginationToken=null;

        do {

            if (faceSearchResult !=null){
                paginationToken = faceSearchResult.getNextToken();
            }


            faceSearchResult  = rek.getFaceSearch(
                    new GetFaceSearchRequest()
                            .withJobId(startJobId)

                            .withNextToken(paginationToken)
                            .withSortBy(FaceSearchSortBy.TIMESTAMP)
            );

            VideoMetadata videoMetaData=faceSearchResult.getVideoMetadata();

            System.out.println("Format: " + videoMetaData.getFormat());
            System.out.println("Codec: " + videoMetaData.getCodec());
            System.out.println("Duration: " + videoMetaData.getDurationMillis());
            System.out.println("FrameRate: " + videoMetaData.getFrameRate());

            //Show search results
            List<PersonMatch> matches= faceSearchResult.getPersons();
            for (PersonMatch match: matches) {

                System.out.println("\t\tPerson number: " + match.getFaceMatches());

                //List <FaceMatch> faceMatches = match.getFaceMatches();
                //System.out.println("Matches in collection...");

                //for (FaceMatch faceMatch : faceMatches){
                 //   Face face=faceMatch.getFace();
                  //  System.out.println("Face Id: "+ face.getFaceId());
                   // System.out.println("Similarity: " + faceMatch.getSimilarity().toString());
                //}
            }

        } while (faceSearchResult.getNextToken() != null);

    }
// end getResults

}

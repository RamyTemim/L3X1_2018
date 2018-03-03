
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


public class DetectFaceInVideo {

  private static   String startJobId=null;

    public static  void DetectFacesInVideos(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId,String queueUrl,AmazonSQS sqs) throws Exception
    {

        //##################################################################################
        StartFaceSearchCollection(bucket,video, rek,  channel, collectionId);//#############
        //##################################################################################

        System.out.println("Waiting for job: " + startJobId);
        //Poll queue for messages
        List<Message> messages;

        boolean jobFound=false;
        //loop until the job status is published. Ignore other messages in queue.
        do
            {
            //Get messages.
            do
                {
                messages = sqs.receiveMessage(queueUrl).getMessages();

            }while(messages.isEmpty());

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
                if(operationJobId.asText().equals(startJobId))
                {
                    jobFound=true;
                    System.out.println("Job id: " + operationJobId );
                    System.out.println("Status : " + operationStatus.toString());
                    if (operationStatus.asText().equals("SUCCEEDED"))
                    {
                        //####################################################
                         GetResultsFaceSearchCollection(startJobId,rek);//####
                        //####################################################
                    }
                    else
                        {
                        System.out.println("Video analysis failed");
                    }
                    sqs.deleteMessage(queueUrl,message.getReceiptHandle());
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

            //Show search results
            List<PersonMatch> matches= faceSearchResult.getPersons();
            for (PersonMatch match: matches)
            {
                if (match.getFaceMatches()!=null && match.getFaceMatches().size()!=0) {
                    //System.out.println("\t\tPerson number: " + match.getFaceMatches());
                    List<FaceMatch> faceMatches = match.getFaceMatches();
                    for (FaceMatch faceMatch : faceMatches)
                    {
                        Face face=faceMatch.getFace();
                        System.out.println( face.getExternalImageId() +" est dans la video ");
                      //  System.out.println("Similarity: " + faceMatch.getSimilarity().toString());
                    }

                }
                /*
                List <FaceMatch> faceMatches = match.getFaceMatches();
                System.out.println("Matches in collection...");
                for (FaceMatch faceMatch : faceMatches)
                 {
                  Face face=faceMatch.getFace();
                  System.out.println("Face Id: "+ face.getFaceId());
                  System.out.println("Similarity: " + faceMatch.getSimilarity().toString());
                }*/
            }

        } while (faceSearchResult.getNextToken() != null);

    }
// end getResults

}

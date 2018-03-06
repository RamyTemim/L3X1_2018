
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;


public class DetectFaceInVideo {

    private static   String startJobId=null;

    /**
     *
     * @param bucket
     * @param video
     * @param rek
     * @param channel
     * @param collectionId
     * @param queueUrl
     * @param sqs
     * @throws Exception
     */
    public static List<String> DetectFacesInVideos(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId,String queueUrl,AmazonSQS sqs) throws Exception
    {

        //##################################################################################
        StartFaceSearchCollection(bucket,video, rek,  channel, collectionId);//#############
        //##################################################################################
        List<String> listnameimage = new ArrayList<String>();
        System.out.println("Waiting for job: " + startJobId);
        //Poll queue for messages
        List<Message> messages;
        int dotLine=0;
        boolean jobFound=false;
        //loop until the job status is published. Ignore other messages in queue.
        do
            {
            //Get messages.
            do
                {
                messages = sqs.receiveMessage(queueUrl).getMessages();

                    if (dotLine++<100){
                        System.out.print(".");
                    }else{
                        System.out.println();
                        dotLine=0;
                    }

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
                System.out.println("Job found was " + operationJobId);
                if(operationJobId.asText().equals(startJobId))
                {
                    jobFound=true;
                    System.out.println("Job id: " + operationJobId );
                    System.out.println("Status : " + operationStatus.toString());
                    if (operationStatus.asText().equals("SUCCEEDED"))
                    {
                        //####################################################
                        listnameimage= GetResultsFaceSearchCollection(startJobId,rek,video);//####
                        //####################################################
                    }
                    else
                        {
                        System.out.println("Video analysis failed");
                    }
                    sqs.deleteMessage(queueUrl,message.getReceiptHandle());
                }else{
                    System.out.println("Job received was not job " +  startJobId);
                }

            }

        } while (!jobFound);
        System.out.println("Done!");
        return listnameimage;
    }// end DetectFacesInVideos


    /**
     *
     * @param bucket
     * @param video
     * @param rek
     * @param channel
     * @param collectionId
     */
    private static void StartFaceSearchCollection(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId)
    {
        StartFaceSearchRequest req = new StartFaceSearchRequest()
                .withCollectionId(collectionId)
                .withVideo(new Video().withS3Object(new S3Object().withBucket(bucket).withName(video)))
                .withNotificationChannel(channel);

        StartFaceSearchResult startPersonCollectionSearchResult = rek.startFaceSearch(req);
        startJobId = startPersonCollectionSearchResult.getJobId();


    }// END Start


    /**
     *
     * @param startJobId
     * @param rek
     */
    private static List<String>   GetResultsFaceSearchCollection(String startJobId, AmazonRekognition rek,String video)
    {
        GetFaceSearchResult faceSearchResult=null;
       String paginationToken=null;
        List<String> nameimage = new ArrayList<String>();
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

                    List<FaceMatch> faceMatches = match.getFaceMatches();
                    for (FaceMatch faceMatch : faceMatches)
                    {
                        Face face= faceMatch.getFace();
                        String im = face.getExternalImageId();
                        if (!nameimage.contains(im))
                        {
                            nameimage.add(im);
                        }

                    }

                }
            }

        } while (faceSearchResult !=null && faceSearchResult.getNextToken() != null);
      return nameimage;
    }// end getResults
}

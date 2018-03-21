package amazon;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetectFaceInVideo {

    private  static String startJobId=null;

    /**
     *Méthode qui detecte les visages des personnes dans une vidéo et les compare a la collection
     * @param bucket Le nom du compartiment dans lequel sont uploader les videos et les images
     * @param video Le nom de la video à analyser
     * @param rek instance du service de reconnaissance de amazon
     * @param channel Service de notification à laquelle Amazon Rekognition publie l'état d'achèvement d'une opération d'analyse vidéo
     * @param collectionId Le nom de la collection qui comporte les images
     * @param queueUrl L'URL de la file d'attente
     * @param sqs Service de mise en file d'attente (Amazon SQS) offre une file d'attente hébergée fiable et hautement évolutive pour le stockage des messages
     * @return listePhoto des noms des images
     */
    public static List<String> DetectFacesInVideos(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId,String queueUrl,AmazonSQS sqs)
    {

        StartFaceSearchCollection(bucket,video, rek,  channel, collectionId);

        List<String> listnameimage = new ArrayList<>();
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
                JsonNode jsonMessageTree = null;
                try {
                    jsonMessageTree = mapper.readTree(notification);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonNode messageBodyText = jsonMessageTree.get("Message");
                ObjectMapper operationResultMapper = new ObjectMapper();
                JsonNode jsonResultTree = null;
                try {
                    jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

                        listnameimage= GetResultsFaceSearchCollection(startJobId,rek,video);

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
        return listnameimage;
    }// end DetectFacesInVideos


    /**
     *
     * @param bucket Le nom du compartiment dans lequel sont uploader les videos et les images
     * @param video Le nom de la video à analyser
     * @param rek instance du service de reconnaissance de amazon
     * @param channel Service de notification à laquelle Amazon Rekognition publie l'état d'achèvement d'une opération d'analyse vidéo
     * @param collectionId Le nom de la collection qui comporte les images
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
     * @param startJobId identifiant de la vidéo
     * @param rek instance du service de reconnaissance de amazon
     * @param video Le nom de la video à analyser
     * @return nom de l'image
     */
    private static List<String>   GetResultsFaceSearchCollection(String startJobId, AmazonRekognition rek,String video)
    {
        GetFaceSearchResult faceSearchResult=null;
       String paginationToken=null;
        List<String> nameimage = new ArrayList<>();
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

        } while (faceSearchResult.getNextToken() != null);
      return nameimage;
    }// end getResults
}

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

import static java.lang.System.*;

public class DetectFaceInVideo {

    private DetectFaceInVideo(){}

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
    public static List<String> detectFacesInVideos(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId,String queueUrl,AmazonSQS sqs) throws  IOException {

        JsonNode messageBodyText;
        JsonNode operationJobId;
        JsonNode operationStatus;
        JsonNode jsonMessageTree;
        JsonNode jsonResultTree;
        startFaceSearchCollection(bucket,video, rek,  channel, collectionId);

        List<String> listnameimage = new ArrayList<>();
        out.println("Waiting for job: " + startJobId);
        //Poll queue for messages
        List<Message> messages;
        boolean jobFound=false;

        do {
            do {
                messages = sqs.receiveMessage(queueUrl).getMessages();
            }while(messages.isEmpty());

            for (Message message: messages)
            {
                String notification = message.getBody();
                ObjectMapper mapper = new ObjectMapper();
                jsonMessageTree = mapper.readTree(notification);
                messageBodyText = jsonMessageTree.get("Message");
                ObjectMapper operationResultMapper = new ObjectMapper();
                jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
                operationJobId = jsonResultTree.get("JobId");
                operationStatus = jsonResultTree.get("Status");
                out.println("Job found was " + operationJobId);

                if(operationJobId.asText().equals(startJobId))
                {
                    jobFound=true;
                    out.println("Job id: " + operationJobId );
                    out.println("Status : " + operationStatus.toString());
                    if (operationStatus.asText().equals("SUCCEEDED"))
                    {
                        listnameimage= getResultsFaceSearchCollection(startJobId,rek);
                    }
                    else
                        {
                        out.println("Video analysis failed");
                    }
                    sqs.deleteMessage(queueUrl,message.getReceiptHandle());
                }else{
                    out.println("Job received was not job " +  startJobId);
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
    private static void startFaceSearchCollection(String bucket, String video, AmazonRekognition rek, NotificationChannel channel,String collectionId)
    {
        StartFaceSearchRequest req = new StartFaceSearchRequest()
                .withCollectionId(collectionId)
                .withVideo(new Video().withS3Object(new S3Object().withBucket(bucket).withName(video)))
                .withNotificationChannel(channel);

        StartFaceSearchResult startPersonCollectionSearchResult = rek.startFaceSearch(req);
        startJobId = startPersonCollectionSearchResult.getJobId();


    }// END Start


    /**
     *Méthode qui
     * @param startJobId identifiant de la vidéo
     * @param rek instance du service de reconnaissance de amazon
     * @return nom de l'image
     */
    private static List<String>   getResultsFaceSearchCollection(String startJobId, AmazonRekognition rek)
    {

        GetFaceSearchResult faceSearchResult= null;
        String paginationToken=null;
        List<String> nameImage = new ArrayList<>();
        do {
               if(faceSearchResult !=null)
                paginationToken = faceSearchResult.getNextToken();

            faceSearchResult  = rek.getFaceSearch(
                    new GetFaceSearchRequest()
                            .withJobId(startJobId)
                            .withNextToken(paginationToken)
                            .withSortBy(FaceSearchSortBy.TIMESTAMP));

            List<PersonMatch> matches= faceSearchResult.getPersons();
            for (PersonMatch match: matches)
            {
                    List<FaceMatch> faceMatches = match.getFaceMatches();
                    for (FaceMatch faceMatch : faceMatches)
                    {
                        Face face= faceMatch.getFace();
                        String imageId = face.getExternalImageId();
                        if (!nameImage.contains(imageId))
                        {
                            nameImage.add(imageId);
                        }
                    }
            }
        } while (faceSearchResult.getNextToken() != null );
      return nameImage;
    }// end getResults
}

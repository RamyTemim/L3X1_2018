/*
 *
 * L3X1 FACIAL RECONGITION COMPARATOR
 *
 * IA as a service (Facial recognition on vidéo)
 *
 * PACKAGE AMAZON
 *
 * Cette classe et la classe principale de ce package
 * dans celle-ci  on vas analyser les vidéos et comparer
 * les visage qui se trouvent dedans aves les visages
 * qu'il y a dans la collection créer dans la classe CreatCollectionFaces
 *
 * Cette classe va lancer une analyse avec la méthode startFaceSearchCollection()
 * puis récupérer les résultas et les traiter dans la méthode getResultsFaceSearchCollection()
 * ces méthode son appeller dans la  detectFacesInVideos()
 *
 * Puis la méthode detectFacesInVideos() est appeller dans la classe AmazonService
 *
 */

package amazon;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import useful.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class DetectFaceInVideo {

    private DetectFaceInVideo(){}

    private  static String startJobId=null;

    /**
     *Méthode qui detecte les visages des personnes dans une vidéo et les compare au
     * visage qui se trouve dans la collection.
     * Methode Fourni par AMAZON DOCUMENTATION
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
        ObjectMapper mapper = new ObjectMapper();
        ObjectMapper operationResultMapper = new ObjectMapper();

        startFaceSearchCollection(bucket,video, rek,  channel, collectionId);

        List<String> listnameimage = new ArrayList<>();
        JsonUtil.log.info("Waiting for job: " + startJobId);
        List<Message> messages;
        boolean jobFound=false;
        do {
            do {
                messages = sqs.receiveMessage(queueUrl).getMessages();
            }while(messages.isEmpty());
            for (Message message: messages)
            {
                String notification = message.getBody();
                jsonMessageTree = mapper.readTree(notification);
                messageBodyText = jsonMessageTree.get("Message");
                jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
                operationJobId = jsonResultTree.get("JobId");
                operationStatus = jsonResultTree.get("Status");
                JsonUtil.log.info("Job found was " + operationJobId);

                if(operationJobId.asText().equals(startJobId))
                {
                    jobFound=true;
                    JsonUtil.log.info("Job id: " + operationJobId );
                    JsonUtil.log.info("Status : " + operationStatus.toString());
                    if (operationStatus.asText().equals("SUCCEEDED"))
                    {
                        listnameimage= getResultsFaceSearchCollection(startJobId,rek);
                    }
                    else
                        {
                            JsonUtil.log.info("Video analysis failed");
                    }
                    sqs.deleteMessage(queueUrl,message.getReceiptHandle());
                }else{
                    JsonUtil.log.info("Job received was not job " +  startJobId);
                }
            }
        } while (!jobFound);
        return listnameimage;
    }// end DetectFacesInVideos


    /**
     * Méthode qui envoie une requete d'analyse de vidéo
     * @param bucket Le nom du compartiment dans lequel sont uploader les videos et les images
     * @param video Le nom de la video à analyser
     * @param rek instance du service de reconnaissance de amazon
     * @param channel Service de notification à laquelle Amazon Rekognition publie l'état d'achèvement d'une opération d'analyse vidéo
     * @param collectionId Le nom de la collection qui comporte les images
     */
    private static void startFaceSearchCollection(String bucket, String video, AmazonRekognition rek, NotificationChannel channel, String collectionId)
    {
        StartFaceSearchRequest req = new StartFaceSearchRequest()
                .withCollectionId(collectionId)
                .withVideo(new Video().withS3Object(new S3Object().withBucket(bucket).withName(video)))
                .withNotificationChannel(channel);

        StartFaceSearchResult startPersonCollectionSearchResult = rek.startFaceSearch(req);
        startJobId = startPersonCollectionSearchResult.getJobId();


    }// END Start


    /**
     *Méthode qui récupére le fichier json qui es le résulta de l'analyse
     * de la viéo et qui parse les fichier pour récupérer les donnée pertinantes
     * @param startJobId identifiant de la vidéo
     * @param rek instance du service de reconnaissance de amazon
     * @return nom de l'image ou aprai le visage qui est sur la vidéos
     */
    private static List<String> getResultsFaceSearchCollection(String startJobId, AmazonRekognition rek)
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

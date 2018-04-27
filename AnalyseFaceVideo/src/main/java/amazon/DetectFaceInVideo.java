package amazon;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE amazon
 * <p>
 * Cette classe et la classe principale de ce package
 * dans celle-ci on va analyser les vidéos et comparer
 * les visages qui se trouvent dedans aves les visages
 * qu'il y a dans la collection créé dans la classe CreateCollectionFaces
 * <p>
 * Cette classe va lancer une analyse avec la méthode startFaceSearchCollection()
 * puis récupérer les résultats et les traiter dans la méthode getResultsFaceSearchCollection()
 * ces méthode sont appellées dans la méthode detectFacesInVideos()
 * <p>
 * Puis la méthode detectFacesInVideos() est appellée dans la classe AmazonService
 */
public class DetectFaceInVideo {

    private DetectFaceInVideo() {
    }

    public static final Logger log = LogManager.getLogger();

    private static String startJobId = null;

    /**
     * Méthode qui detecte les visages des personnes dans une vidéo et les compare aux
     * visages qui se trouven dans la collection.
     * Methode fourni par AMAZON DOCUMENTATION
     *
     * @param bucket       Le nom du compartiment dans lequel sont uploadé les vidéos et les images
     * @param video        Le nom de la vidéo à analyser
     * @param rek          Instance du service de reconnaissance de amazon
     * @param channel      Service de notification à laquelle Amazon Rekognition publie l'état d'achèvement d'une opération d'analyse vidéo
     * @param collectionId Le nom de la collection qui comporte les images
     * @param queueUrl     L'URL de la file d'attente
     * @param sqs          Service de mise en file d'attente (Amazon SQS) qui offre une file d'attente hébergée fiable et hautement évolutive pour le stockage des messages
     * @return listePhoto des noms des images
     */
    public static List<String> detectFacesInVideos(String bucket, String video, AmazonRekognition rek, NotificationChannel channel, String collectionId, String queueUrl, AmazonSQS sqs) {

        startFaceSearchCollection(bucket, video, rek, channel, collectionId);
        List<String> listnameimage = new ArrayList<>();
        log.info("Waiting for job: " + startJobId);
        //Poll queue for messages
        List<Message> messages;
        boolean jobFound = false;
        //loop until the job status is published. Ignore other messages in queue.
        do {
            //Get messages.
            do {
                messages = sqs.receiveMessage(queueUrl).getMessages();
            } while (messages.isEmpty());
            //Loop through messages received.
            for (Message message : messages) {
                String notification = message.getBody();
                // Get status and job id from notification.
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonMessageTree = null;
                try {
                    jsonMessageTree = mapper.readTree(notification);
                } catch (IOException e) {
                    log.info("Erreur lors de la lecture du fichier dans la méthode detectFacesInVideos : ");
                }
                JsonNode messageBodyText = null;
                if (jsonMessageTree != null) {
                    messageBodyText = jsonMessageTree.get("Message");
                }
                ObjectMapper operationResultMapper = new ObjectMapper();
                JsonNode jsonResultTree = null;
                try {
                    if (messageBodyText != null) {
                        jsonResultTree = operationResultMapper.readTree(messageBodyText.textValue());
                    }
                } catch (IOException e) {
                    log.info("Erreur lors de la lecture du fichier dans la méthode detectFacesInVideos : ");
                }
                JsonNode operationJobId = null;
                if (jsonResultTree != null) {
                    operationJobId = jsonResultTree.get("JobId");
                }
                JsonNode operationStatus = null;
                if (jsonResultTree != null) {
                    operationStatus = jsonResultTree.get("Status");
                }
                log.info("Job found was " + operationJobId);
                if (operationJobId != null) {
                    if (operationJobId.asText().equals(startJobId)) {
                        jobFound = true;
                        log.info("id: du job  " + operationJobId);
                        log.info("Status du job : " + operationStatus.toString());
                        if (operationStatus.asText().equals("SUCCEEDED")) {
                            listnameimage = getResultsFaceSearchCollection(startJobId, rek);
                        } else {
                            log.info("Video analysis failed");
                        }
                        sqs.deleteMessage(queueUrl, message.getReceiptHandle());
                    } else {
                        log.info("Job received was not job " + startJobId);
                    }
                }
            }
        } while (!jobFound);
        return listnameimage;
    }// end DetectFacesInVideos


    /**
     * Méthode qui envoit une requete d'analyse de vidéo
     *
     * @param bucket       Le nom du compartiment dans lequel sont uploadé les vidéos et les images
     * @param video        Le nom de la vidéo à analyser
     * @param rek          instance du service de reconnaissance de amazon
     * @param channel      Service de notification à laquelle Amazon Rekognition publie l'état d'achèvement d'une opération d'analyse vidéo
     * @param collectionId Le nom de la collection qui comporte les images
     */
    private static void startFaceSearchCollection(String bucket, String video, AmazonRekognition rek, NotificationChannel channel, String collectionId) {
        StartFaceSearchRequest req = new StartFaceSearchRequest()
                .withCollectionId(collectionId)
                .withVideo(new Video().withS3Object(new S3Object().withBucket(bucket).withName(video)))
                .withNotificationChannel(channel);

        StartFaceSearchResult startPersonCollectionSearchResult = rek.startFaceSearch(req);
        startJobId = startPersonCollectionSearchResult.getJobId();


    }// END Start


    /**
     * Méthode qui récupére le fichier json qui est le résultat de l'analyse
     * de la vidéo et qui parse les fichier pour récupérer les donnéed pertinantes
     * @param startJobId identifiant de la vidéo
     * @param rek        instance du service de reconnaissance de amazon
     * @return nom de l'image ou aparait le visage qui est sur la vidéo
     */
    private static List<String> getResultsFaceSearchCollection(String startJobId, AmazonRekognition rek) {

        GetFaceSearchResult faceSearchResult = null;
        String paginationToken = null;
        List<String> nameImage = new ArrayList<>();
        do {
            if (faceSearchResult != null)
                paginationToken = faceSearchResult.getNextToken();

            faceSearchResult = rek.getFaceSearch(
                    new GetFaceSearchRequest()
                            .withJobId(startJobId)
                            .withNextToken(paginationToken)
                            .withSortBy(FaceSearchSortBy.TIMESTAMP));

            List<PersonMatch> matches = faceSearchResult.getPersons();
            for (PersonMatch match : matches) {
                if (match.getFaceMatches() != null && match.getFaceMatches().size() != 0) {
                    List<FaceMatch> faceMatches = match.getFaceMatches();
                    for (FaceMatch faceMatch : faceMatches) {
                        Face face = faceMatch.getFace();
                        String imageId = face.getExternalImageId();
                        if (!nameImage.contains(imageId)) {
                            nameImage.add(imageId);
                        }
                    }
                }
            }
        } while (faceSearchResult.getNextToken() != null);
        return nameImage;
    }// end getResults
}

/*
 * L3X1 FACIAL RECONGITION COMPARATOR
 *
 * IA as a service (Facial recognition on vidéo)
 *
 * PACKAGE identification
 *
 *  Cette Classe est une classe constante
 *
 * Elle contient les constante qui sont les clées d'accés
 * à l'API d'Amazon
 */


package identification;

import springboot.service.AmazonServices;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.NotificationChannel;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;


public final class KeyAmazonApi {
    private KeyAmazonApi(){}

    // nom de la collection ou seront stocker les métha-données des visages
    public  static final String  COLLECTIONID= "yoni";

    // le nom du compartiment S3 pour les photos
    public  static final String BUCKETPHOTO = "analysphoto";

    // le nom du compartiment S3 pour les videos
    public  static final String BUCKETVIDEO = "analysvideo";

    // l'url du sqs
    public  static final String QUEUEURL = "";

    public  static final NotificationChannel CHANNEL = new NotificationChannel().withSNSTopicArn("")
            .withRoleArn("");

    public  static final AmazonSQS SQS = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(AmazonServices.credentials)).build();

    public  static final AmazonRekognition REK = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("", "")).build();


}


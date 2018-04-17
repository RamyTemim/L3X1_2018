package identification;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.NotificationChannel;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import springboot.service.AmazonServices;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE identification
 * <p>
 * Cette classe est une classe qui a pour but de stocker les constantes
 * <p>
 * Elle contient toutes les clés d'accès à l'API d'Amazon
 */
public final class KeyAmazonApi {
    private KeyAmazonApi() {
    }

    // nom de la collection ou seront stocker les métha-données des visages
    public static final String COLLECTIONID = "yoni";

    // le nom du compartiment S3 pour les photos
    public static final String BUCKETPHOTO = "analysphoto";

    // le nom du compartiment S3 pour les videos
    public static final String BUCKETVIDEO = "analysvideo";

    // l'url du sqs
    public static final String QUEUEURL = "";

    public static final NotificationChannel CHANNEL = new NotificationChannel().withSNSTopicArn("")
            .withRoleArn("");

    public static final AmazonSQS SQS = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(AmazonServices.credentials)).build();

    public static final AmazonRekognition REK = AmazonRekognitionClientBuilder.standard().withCredentials(new ProfileCredentialsProvider())
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("", "")).build();


}


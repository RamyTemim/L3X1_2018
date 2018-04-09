


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
    public  static final String BUCKETPHOTO = "lxphoto";

    // le nom du compartiment S3 pour les videos
    public  static final String BUCKETVIDEO = "lxvideo";

    // l'url du sqs
    public  static final String QUEUEURL = "{Your URL }";

    public  static final NotificationChannel CHANNEL = new NotificationChannel().withSNSTopicArn("{Your SNS}")
            .withRoleArn("{Your ARN}");

    public  static final AmazonSQS SQS = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(AmazonServices.credentials)).build();

    public  static final AmazonRekognition REK = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://rekognition.us-east-1.amazonaws.com", "us-east-1")).build();







}

package amazon;

import SpringBoot.AmazonServices;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.NotificationChannel;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;


public interface Var {

    // nom de la collection ou seront stocker les métha-données des visages
    String  collectionId = "yoni";

    // le nom du compartiment S3 pour les photos
    String  bucketPhoto = "lxphoto";

    // le nom du compartiment S3 pour les videos
    String  bucketVideo = "lxvideo";

    // l'url du sqs
    String  queueUrl = "";

    NotificationChannel channel = new NotificationChannel().withSNSTopicArn("")
            .withRoleArn("");

    AmazonSQS sqs = AmazonSQSClientBuilder.standard().withRegion(Regions.US_EAST_1).withCredentials(new AWSStaticCredentialsProvider(AmazonServices.credentials)).build();

    AmazonRekognition rek = AmazonRekognitionClientBuilder.standard().withCredentials( new ProfileCredentialsProvider())
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("https://rekognition.us-east-1.amazonaws.com", "us-east-1")).build();







}

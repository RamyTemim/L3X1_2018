package springboot.service;

import amazon.CreateCollectionFaces;
import amazon.DetectFaceInVideo;
import amazon.S3operation;
import com.amazonaws.auth.AWSCredentials;
import identification.KeyAmazonApi;
import org.springframework.stereotype.Service;
import springboot.model.AmazonModel;
import springboot.model.Persons;

import java.util.ArrayList;
import java.util.List;

/**
 * L3X1 FACIAL RECONGITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on vidéo)
 * <p>
 * PACKAGE service
 * <p>
 * Cette classe correspond au service du MVC pour tout les traitements correspondant à l'API de Amazon
 */
@Service
public class AmazonServices {
    private AmazonServices() {
    }

    public static final AWSCredentials credentials = CreateCollectionFaces.connexionIndexFace();

    public AmazonModel getJson(List<String> pathPhoto, List<String> pathVideo) {

        S3operation.creatBucket(KeyAmazonApi.BUCKETPHOTO);

        for (String aListpathTophoto : pathPhoto) {
            S3operation.uploadFileToBucket(KeyAmazonApi.BUCKETPHOTO, aListpathTophoto);
        }

        S3operation.creatBucket(KeyAmazonApi.BUCKETVIDEO);


        for (String aListpathToVideo : pathVideo) {
            S3operation.uploadFileToBucket(KeyAmazonApi.BUCKETVIDEO, aListpathToVideo);
        }

        CreateCollectionFaces.deleteCollection(KeyAmazonApi.COLLECTIONID, credentials);

        CreateCollectionFaces.createCollectionFace(credentials, KeyAmazonApi.COLLECTIONID);

        List<String> listnameOfImage = S3operation.listFilesInBucket(KeyAmazonApi.BUCKETPHOTO);

        for (String aListnameOfImage : listnameOfImage) {
            CreateCollectionFaces.addFace(credentials, KeyAmazonApi.BUCKETPHOTO, aListnameOfImage, KeyAmazonApi.COLLECTIONID);
        }

        List<String> listnameOfVideos = S3operation.listFilesInBucket(KeyAmazonApi.BUCKETVIDEO);

        List<List<String>> listImageInVideos = new ArrayList<>();


        for (int i = 0; i < listnameOfVideos.size(); i++) {
            listImageInVideos.add(DetectFaceInVideo.detectFacesInVideos(KeyAmazonApi.BUCKETVIDEO, listnameOfVideos.get(i), KeyAmazonApi.REK, KeyAmazonApi.CHANNEL, KeyAmazonApi.COLLECTIONID, KeyAmazonApi.QUEUEURL, KeyAmazonApi.SQS));
        }

        AmazonModel amazonModel = new AmazonModel();
        Persons persons = new Persons();


        for (String aListnameOfImage : listnameOfImage) {
            List<String> list = new ArrayList<>();

            for (int j = 0; j < listImageInVideos.size(); j++) {
                if (listImageInVideos.get(j).contains(aListnameOfImage))
                    list.add(listnameOfVideos.get(j));
            }
            persons.setName(aListnameOfImage);
            persons.setVideos(list);
            amazonModel.addPerson(persons);
        }

        S3operation.purgeBucket(KeyAmazonApi.BUCKETPHOTO);
        S3operation.purgeBucket(KeyAmazonApi.BUCKETVIDEO);

        return amazonModel;
    }


}

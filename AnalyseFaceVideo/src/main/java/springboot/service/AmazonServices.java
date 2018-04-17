package springboot.service;

import identification.KeyAmazonApi;
import springboot.model.AmazonModel;
import springboot.model.Persons;
import amazon.*;
import com.amazonaws.auth.AWSCredentials;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;


@Service
public class AmazonServices {
private AmazonServices(){}

     public static   AWSCredentials credentials = CreatCollectionFaces.connexionIdexFace();

     public AmazonModel getJson(List<String> pathPhoto, List<String> pathVideo) throws IOException {

         /*S3operation.creatBucket(KeyAmazonApi.BUCKETPHOTO);

        for (String aListpathTophoto : pathPhoto)
        {
            S3operation.uploadFileToBucket(KeyAmazonApi.BUCKETPHOTO, aListpathTophoto);
        }

        S3operation.creatBucket(KeyAmazonApi.BUCKETVIDEO);


        for (String aListpathToVideo : pathVideo)
        {
            S3operation.uploadFileToBucket(KeyAmazonApi.BUCKETVIDEO, aListpathToVideo);
        }
*/
        CreatCollectionFaces.deleteCollection(KeyAmazonApi.COLLECTIONID, credentials);

        CreatCollectionFaces.creatCollectionFace(credentials, KeyAmazonApi.COLLECTIONID);

        List<String> listnameOfImage = S3operation.listFilesInBucket(KeyAmazonApi.BUCKETPHOTO);

         for (String aListnameOfImage : listnameOfImage) {
             CreatCollectionFaces.addFace(credentials, KeyAmazonApi.BUCKETPHOTO, aListnameOfImage, KeyAmazonApi.COLLECTIONID);
         }

        List<String> listnameOfVideos = S3operation.listFilesInBucket(KeyAmazonApi.BUCKETVIDEO);

        List<List<String>> listImageInVideos = new ArrayList<>();


         for (int i =0 ; i<listnameOfVideos.size();i++)   {
             listImageInVideos.add(DetectFaceInVideo.detectFacesInVideos(KeyAmazonApi.BUCKETVIDEO, listnameOfVideos.get(i), KeyAmazonApi.REK, KeyAmazonApi.CHANNEL, KeyAmazonApi.COLLECTIONID, KeyAmazonApi.QUEUEURL, KeyAmazonApi.SQS));
         }

         AmazonModel amazonModel = new AmazonModel();
         Persons persons = new Persons();


         for (String aListnameOfImage : listnameOfImage)
         {
             List<String> list = new ArrayList<>();

             for (int j = 0; j < listImageInVideos.size(); j++)
             {
                 if (listImageInVideos.get(j).contains(aListnameOfImage))
                     list.add(listnameOfVideos.get(j));
             }
             persons.setName(aListnameOfImage);
             persons.setVideos(list);
             amazonModel.addPerson(persons);
         }

        //S3operation.purgeBucket(KeyAmazonApi.BUCKETPHOTO);
        //S3operation.purgeBucket(KeyAmazonApi.BUCKETVIDEO);

        return amazonModel;
    }


}

package springboot.service;

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

     public static final  AWSCredentials credentials = CreatCollectionFaces.connexionIdexFace();

     public static AmazonModel getJson(List<String> pathPhoto, List<String> pathVideo) throws IOException {

        S3operation.creatBucket(VarAma.BUCKETPHOTO);

        for (String aListpathTophoto : pathPhoto)
        {
            S3operation.uploadFileToBucket(VarAma.BUCKETPHOTO, aListpathTophoto);
        }

        S3operation.creatBucket(VarAma.BUCKETVIDEO);


        for (String aListpathToVideo : pathVideo)
        {
            S3operation.uploadFileToBucket(VarAma.BUCKETVIDEO, aListpathToVideo);
        }

        CreatCollectionFaces.deleteCollection(VarAma.COLLECTIONID, credentials);

        CreatCollectionFaces.creatCollectionFace(credentials, VarAma.COLLECTIONID);

        List<String> listnameOfImage = S3operation.listFilesInBucket(VarAma.BUCKETPHOTO);

        for (int i=0; i<listnameOfImage.size();i++)
        {
            CreatCollectionFaces.addFace(credentials, VarAma.BUCKETPHOTO, listnameOfImage.get(i) , VarAma.COLLECTIONID);
        }

        List<String> listnameOfVideos = S3operation.listFilesInBucket(VarAma.BUCKETVIDEO);

        List<List<String>> listImageInVideos = new ArrayList<>();


        for (int i =0 ; i<listnameOfVideos.size();i++)
        {
            listImageInVideos.add(DetectFaceInVideo.detectFacesInVideos(VarAma.BUCKETVIDEO, listnameOfVideos.get(i), VarAma.REK, VarAma.CHANNEL, VarAma.COLLECTIONID, VarAma.QUEUEURL, VarAma.SQS));
        }


         AmazonModel amazonModel = new AmazonModel();
         Persons persons = new Persons();


        for (int i=0; i<listnameOfImage.size(); i++)
        {
            List <String> list = new ArrayList<>();

            for ( int j=0; j<listImageInVideos.size(); j++ )
            {
                if (listImageInVideos.get(j).contains(listnameOfImage.get(i)))
                {
                    list.add(listnameOfVideos.get(j));
                }
            }
          persons.setName(listnameOfImage.get(i));
          persons.setVideos(list);
          amazonModel.addPerson(persons);
        }




        S3operation.purgeBucket(VarAma.BUCKETPHOTO);
        S3operation.purgeBucket(VarAma.BUCKETVIDEO);

        return amazonModel;
    }


}

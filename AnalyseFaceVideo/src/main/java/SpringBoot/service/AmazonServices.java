package SpringBoot.service;

import SpringBoot.model.AmazonModel;
import SpringBoot.model.FacialRecongition;
import SpringBoot.model.Persons;
import amazon.*;

import com.amazonaws.auth.AWSCredentials;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.Iterator;
import java.util.List;


@Service
public class AmazonServices {

    public static AWSCredentials credentials;

    public String getJson(List<String> pathPhoto, List<String> pathVideo)  {

        S3operation.CreatBucket(Var.bucketPhoto);

        for (String aListpathTophoto : pathPhoto)
        {
            S3operation.UploadFileToBucket(Var.bucketPhoto, aListpathTophoto);
        }

        S3operation.CreatBucket(Var.bucketVideo);


        for (String aListpathToVideo : pathVideo)
        {
            S3operation.UploadFileToBucket(Var.bucketVideo, aListpathToVideo);
        }

        credentials = CreatCollectionFaces.connexionIdexFace();

        CreatCollectionFaces.DeleteCollection(Var.collectionId, credentials);

        CreatCollectionFaces.CreatCollectionFace(credentials, Var.collectionId);

        List<String> listnameOfImage = S3operation.ListFilesInBucket(Var.bucketPhoto);

        for (int i=0; i<listnameOfImage.size();i++)
        {
            CreatCollectionFaces.addFace(credentials, Var.bucketPhoto, listnameOfImage.get(i) , Var.collectionId);
        }

        List<String> listnameOfVideos = S3operation.ListFilesInBucket(Var.bucketVideo);

        List<List<String>> listImageInVideos = new ArrayList<>();


        for (int i =0 ; i<listnameOfVideos.size();i++)
        {
            listImageInVideos.add(DetectFaceInVideo.DetectFacesInVideos(Var.bucketVideo, listnameOfVideos.get(i), Var.rek, Var.channel, Var.collectionId, Var.queueUrl, Var.sqs));
        }



        JSONObject jsonObjectListFinal = new JSONObject();
        for (int i=0; i<listnameOfImage.size(); i++)
        {
            JSONArray jsonArrayListVideo = new JSONArray();
            for ( int j=0; j<listImageInVideos.size(); j++ )
            {
                if (listImageInVideos.get(j).contains(listnameOfImage.get(i)))
                {
                    jsonArrayListVideo.put(listnameOfVideos.get(j));
                }
            }
            jsonObjectListFinal.put(listnameOfImage.get(i), jsonArrayListVideo);
        }

        Persons persons = null;
        AmazonModel amazonModel=null;
        for (Iterator iterator = jsonObjectListFinal.keys(); iterator.hasNext();)
        {
            //persons.nameOfPerson = iterator.next();
            //persons.listOfVideos = jsonObjectListFinal.get(String.valueOf(persons.nameOfPerson));
            amazonModel.persons.add(persons);

        }
        FacialRecongition.amazonModel = amazonModel;


        S3operation.PurgeBucket(Var.bucketPhoto);
        S3operation.PurgeBucket(Var.bucketVideo);

        return FacialRecongition.amazonModel.toString();
    }


}


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResultOnJson {


    public static JSONObject getJsonObjectAmazon() throws Exception {

        List<String> listpathTophoto = JsonUtil.readFile(VideoDetect.pathPhoto);
        List<String> listpathToVideo = JsonUtil.readFile(VideoDetect.pathVideo);

        S3operation.CreatBucket(Var.bucketPhoto);
        for (String aListpathTophoto : listpathTophoto)
        {
            S3operation.UploadFileToBucket(Var.bucketPhoto, aListpathTophoto);
        }

        S3operation.CreatBucket(Var.bucketVideo);
        for (String aListpathToVideo : listpathToVideo)
        {
            S3operation.UploadFileToBucket(Var.bucketVideo, aListpathToVideo);
        }

        VideoDetect.credentials =CreatCollectionFaces.connexionIdexFace();
        CreatCollectionFaces.DeleteCollection(Var.collectionId,VideoDetect.credentials);
        CreatCollectionFaces.CreatCollectionFace(VideoDetect.credentials, Var.collectionId);

        List<String> listnameOfImage = S3operation.ListFilesInBucket(Var.bucketPhoto);
        for (int i=0; i<listnameOfImage.size();i++)
        {
            CreatCollectionFaces.addFace(VideoDetect.credentials, Var.bucketPhoto, listnameOfImage.get(i) ,Var.collectionId);
        }

        List<String> listnameOfVideos = S3operation.ListFilesInBucket(Var.bucketVideo);
        List<List<String>> listImageInVideos = new ArrayList<List<String>>();

        for (int i =0 ; i<listnameOfVideos.size();i++)
        {
            listImageInVideos.add(DetectFaceInVideo.DetectFacesInVideos(Var.bucketVideo, listnameOfVideos.get(i), Var.rek, Var.channel, Var.collectionId, Var.queueUrl, Var.sqs));
        }

        for (int i=0; i<listnameOfImage.size(); i++)
        {
            for ( int j=0; j<listImageInVideos.size(); j++ )
            {
                if (listImageInVideos.get(j).contains(listnameOfImage.get(i)))
                {
                    Var.valeuOfValeuAmazon.append(listnameOfImage.get(i) , listnameOfVideos.get(j) );
                }
            }
        }
        Var.valueAmazon.put(Var.valeuOfValeuAmazon);
        Var.jsonObjectAmazon.append("Amazon", Var.valueAmazon);
       //System.out.println(Var.jsonObjectAmazon);
        return Var.jsonObjectAmazon;
    }// END getJsonObjectAmazon

}

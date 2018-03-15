package SpringBoot;

import amazon.*;

import microsoft.JsonUtil;
import com.amazonaws.auth.AWSCredentials;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.Scanner;

@Service
public class AmazonServices {

   public static   AWSCredentials credentials;
   public static String  pathPhoto;
   public static String  pathVideo;

 public AmazonServices()
  {

  }
    public static  void  readfileOfpath ()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemin pour accéder au fichier qui contient les photos  :");
        pathPhoto = sc.nextLine();
        System.out.println("Veuillez saisir le chemin pour accéder au fichier qui contient les videos :");
        pathVideo = sc.nextLine();
    }

    static String getJsonObjectAmazon() throws Exception {


        List<String> listpathTophoto = JsonUtil.readFile(pathPhoto);
        List<String> listpathToVideo = JsonUtil.readFile(pathVideo);

        // création du compartiment S3 pour les photos
        S3operation.CreatBucket(Var.bucketPhoto);

        // chargement des photos das le compartiment des photos
        for (String aListpathTophoto : listpathTophoto)
        {
            S3operation.UploadFileToBucket(Var.bucketPhoto, aListpathTophoto);
        }
        // création du compartiment S3 pour les videos
        S3operation.CreatBucket(Var.bucketVideo);

        // chargement des photos das le compartiment des videos
        for (String aListpathToVideo : listpathToVideo)
        {
            S3operation.UploadFileToBucket(Var.bucketVideo, aListpathToVideo);
        }

        // vérification des données d'authentificaton et des aurorisation d'accée
        credentials = CreatCollectionFaces.connexionIdexFace();

        //Supprimer l'anciene collection
        CreatCollectionFaces.DeleteCollection(Var.collectionId, credentials);
        // création d'une nouvelle  collection
        CreatCollectionFaces.CreatCollectionFace(credentials, Var.collectionId);



        // récupérer les nom des photos dans le bucket photo
        List<String> listnameOfImage = S3operation.ListFilesInBucket(Var.bucketPhoto);

        // Ajout des visages dans la collection
        for (int i=0; i<listnameOfImage.size();i++)
        {
            CreatCollectionFaces.addFace(credentials, Var.bucketPhoto, listnameOfImage.get(i) , Var.collectionId);
        }

        // liste qui contient les nom des videos qui se trouve dasn le compartiment videos
        List<String> listnameOfVideos = S3operation.ListFilesInBucket(Var.bucketVideo);

        List<List<String>> listImageInVideos = new ArrayList<>();


        // detecter les personnes qui se trouve dans chaque video en utilise la méthode DetectFacesInVideos
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
            jsonObjectListFinal.put(listnameOfImage.get(i),jsonArrayListVideo);
        }


        S3operation.PurgeBucket(Var.bucketPhoto);
        S3operation.PurgeBucket(Var.bucketVideo);

        return jsonObjectListFinal.toString();
    }// END getJsonObjectAmazon


}

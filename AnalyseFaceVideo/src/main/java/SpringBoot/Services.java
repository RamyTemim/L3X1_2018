package SpringBoot;

import amazon.*;
import microsoft.JsonUtil;
import com.amazonaws.auth.AWSCredentials;

import microsoft.MethodMain;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;



@org.springframework.stereotype.Service
public class Services {

   public static   AWSCredentials credentials;
   public static String  pathPhoto;
   public static String  pathVideo;

 public Services()
  {

  }

    public static  void  readfileOfpath ()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Veuillez saisir le chemain pour accéder au fichier qui contient les photos  :");
        pathPhoto = sc.nextLine();
        System.out.println("Veuillez saisire e chemain pour accéder au fichier qui contient les videos :");
        pathVideo = sc.nextLine();
    }

    static String getJsonObjectAmazon() throws Exception {




//####################################################################################################################
       /* Scanner sc = new Scanner(System.in);                                                                //########
                                                                                                            //########
        System.out.println("Veuillez saisir le chemain pour accéder au fichier qui contient les photos  :");//########
        pathPhoto = sc.nextLine();                                                                          //########
                                                                                                            //########
        System.out.println("Veuillez saisire e chemain pour accéder au fichier qui contient les videos :"); //########
        pathVideo = sc.nextLine(); */                                                                         //########
//####################################################################################################################

 //####################################1#################################################
        // création d'une liste qui contient les chemain vers les photos        //#######
        List<String> listpathTophoto = JsonUtil.readFile(pathPhoto);            //#######
        // création d'une liste qui contient les chemain vers les photos        //#######
        List<String> listpathToVideo = JsonUtil.readFile(pathVideo);            //#######
 //######################################################################################

 //####################################2#################################################
        // création du compartiment S3 pour les photos                        //#########
        S3operation.CreatBucket(Var.bucketPhoto);                             //#########
                                                                              //#########
        // chargement des photos das le compartiment des photos               //#########
        for (String aListpathTophoto : listpathTophoto)                       //#########
        {                                                                     //#########
            S3operation.UploadFileToBucket(Var.bucketPhoto, aListpathTophoto);//#########
        }                                                                     //#########
        // création du compartiment S3 pour les videos                        //#########
        S3operation.CreatBucket(Var.bucketVideo);                             //#########
                                                                              //#########
        // chargement des photos das le compartiment des videos               //#########
        for (String aListpathToVideo : listpathToVideo)                       //#########
        {                                                                     //#########
            S3operation.UploadFileToBucket(Var.bucketVideo, aListpathToVideo);//#########
        }                                                                     //#########
 //######################################################################################

 //#####################################3############################################################
        // vérification des données d'authentificaton et des aurorisation d'accée           //#######
        credentials = CreatCollectionFaces.connexionIdexFace();                             //#######
                                                                                            //#######
        //Supprimer l'anciene collection                                                    //#######
        CreatCollectionFaces.DeleteCollection(Var.collectionId, credentials);               //#######
        // création d'une nouvelle  collection                                              //#######
        CreatCollectionFaces.CreatCollectionFace(credentials, Var.collectionId);            //#######
 //##################################################################################################

 //###########################################4########################################################################################
        // récupérer les nom des photos dans le bucket photo                                                                  //#######
        List<String> listnameOfImage = S3operation.ListFilesInBucket(Var.bucketPhoto);                                        //#######
                                                                                                                              //#######
        // Ajout des visages dans la collection                                                                               //#######
        for (int i=0; i<listnameOfImage.size();i++)                                                                           //#######
        {                                                                                                                     //#######
            CreatCollectionFaces.addFace(credentials, Var.bucketPhoto, listnameOfImage.get(i) , Var.collectionId);            //#######
        }                                                                                                                     //#######
 //####################################################################################################################################

//################################################5############################################################################################################################################
        // liste qui contient les nom des videos qui se trouve dasn le compartiment videos                                                                                        //###########
        List<String> listnameOfVideos = S3operation.ListFilesInBucket(Var.bucketVideo);                                                                                           //###########
        // liste pour stocker les les personnes qui sont dans chaque video                                                                                                        //###########
        List<List<String>> listImageInVideos = new ArrayList<>();                                                                                                                 //###########
                                                                                                                                                                                  //###########
        // detecter les personnes qui se trouve dans chaque video en utilise la méthode DetectFacesInVideos                                                                       //###########                                                                                                     //###########
        for (int i =0 ; i<listnameOfVideos.size();i++)                                                                                                                            //###########
        {                                                                                                                                                                         //###########
            listImageInVideos.add(DetectFaceInVideo.DetectFacesInVideos(Var.bucketVideo, listnameOfVideos.get(i), Var.rek, Var.channel, Var.collectionId, Var.queueUrl, Var.sqs));//###########
        }                                                                                                                                                                         //###########
        for (int i=0; i<listnameOfImage.size(); i++)                                                                                                                              //###########
        {                                                                                                                                                                         //###########
            for ( int j=0; j<listImageInVideos.size(); j++ )                                                                                                                      //###########
            {                                                                                                                                                                     //###########
                if (listImageInVideos.get(j).contains(listnameOfImage.get(i)))                                                                                                    //###########
                {                                                                                                                                                                 //###########
                    Var.valeuOfValeuAmazon.append(listnameOfImage.get(i) , listnameOfVideos.get(j) );                                                                             //###########
                }                                                                                                                                                                 //###########
            }                                                                                                                                                                     //###########
        }                                                                                                                                                                         //###########
        Var.valueAmazon.put(Var.valeuOfValeuAmazon);                                                                                                                              //###########
        Var.jsonObjectAmazon.append("Amazon", Var.valueAmazon);                                                                                                                   //###########
 //############################################################################################################################################################################################

 //########################6##############################
        // vider le bucketphoto                  //#######
        S3operation.PurgeBucket(Var.bucketPhoto);//#######
        // vider le bucketvideo                  //#######
        S3operation.PurgeBucket(Var.bucketVideo);//#######
 //#######################################################

        return Var.jsonObjectAmazon.toString(2);
    }// END getJsonObjectAmazon


/*
    public String getJSON() throws IOException {
        // Scanner sc = new Scanner(System.in);
        //System.out.println("Veuillez saisir le path pour accéder au fichier contenant les photos :");
        //String pathPhoto = sc.nextLine();

        // sc = new Scanner(System.in);
        // System.out.println("Veuillez saisir le path pour accéder au fichier contenant les vidéos :");
        //String pathVideo = sc.nextLine();

        // Pour récupérer les paths des vidéos se trouvant dans le fichier passe en paramètre
        List<String> videoPath = JsonUtil.readFile(pathVideo);

        // Pour uploader les vidéos se trouvant dans le fichier listeVidéo et récupérer les ids des vidéos
        // List<String> videoIds = MethodMain.uploadVideo(videoPath);

        // Pour avoir une liste de vidéos déja indexés sans avoir à les uploader auparavant
        List<String> videoIds = Arrays.asList("8f6aa69ebc", "7c26a90418", "b84e9c7f30");
        //System.out.println(videoIds.toString());


        //Pour afficher l'ensemble des photos detectes dans chaque vidéo
        //System.out.println(JsonUtil.getListLienVideo(videoIds));

        // Pour récupérer les paths des photos se trouvant dans le fichier passe en paramètre
        List<String> photoPath = JsonUtil.readFile(pathPhoto);

        // Création de toutes les Facelist pour chaque vidéo (stockés dans un id , 0 pour la 1ere video, ...)
        MethodMain.createFaceListFromPhotosOnVideo(videoIds, videoPath);

        // Pour lancer la comparaison entre toutes les photos et les vidéos
        JSONObject jsonResultat = MethodMain.detectFaceWithVideoAndPhoto(photoPath, videoIds.size(), videoPath);
        //System.out.println("\n\n"+jsonResultat.toString(2));
        return jsonResultat.toString();
    }
*/
}

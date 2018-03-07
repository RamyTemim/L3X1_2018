
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResultOnJson {


    public static JSONObject getJsonObjectAmazon() throws Exception {


         String  pathPhoto;
         String  pathVideo;

//####################################################################################################################
        Scanner sc = new Scanner(System.in);                                                                //########
                                                                                                            //########
        System.out.println("Veuillez saisir le chemain pour accéder au fichier qui contient les photos  :");//########
        pathPhoto = sc.nextLine();                                                                          //########
                                                                                                            //########
        System.out.println("Veuillez saisire e chemain pour accéder au fichier qui contient les videos :"); //########
        pathVideo = sc.nextLine();                                                                          //########
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
        VideoDetect.credentials =CreatCollectionFaces.connexionIdexFace();                  //#######
                                                                                            //#######
        //Supprimer l'anciene collection                                                    //#######
        CreatCollectionFaces.DeleteCollection(Var.collectionId,VideoDetect.credentials);    //#######
        // création d'une nouvelle  collection                                              //#######
        CreatCollectionFaces.CreatCollectionFace(VideoDetect.credentials, Var.collectionId);//#######
 //##################################################################################################

 //###########################################4#######################################################################################
        // récupérer les nom des photos dans le bucket photo                                                                 //#######
        List<String> listnameOfImage = S3operation.ListFilesInBucket(Var.bucketPhoto);                                       //#######
                                                                                                                             //#######
        // Ajout des visages dans la collection                                                                              //#######
        for (int i=0; i<listnameOfImage.size();i++)                                                                          //#######
        {                                                                                                                    //#######
            CreatCollectionFaces.addFace(VideoDetect.credentials, Var.bucketPhoto, listnameOfImage.get(i) ,Var.collectionId);//#######
        }                                                                                                                    //#######
 //###################################################################################################################################

 //################################################5###########################################################################################################################################
        // liste qui contient les nom des videos qui se trouve dasn le compartiment videos
        List<String> listnameOfVideos = S3operation.ListFilesInBucket(Var.bucketVideo);                                                                                           //###########
        // liste pour stocker les les personnes qui sont dans chaque video
        List<List<String>> listImageInVideos = new ArrayList<>();                                                                                                     //###########

        // detecter les personnes qui se trouve dans chaque video en utilise la méthode DetectFacesInVideos                                                                                                                                                                          //###########
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
        return Var.jsonObjectAmazon;
    }// END getJsonObjectAmazon

}

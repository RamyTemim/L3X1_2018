package yoniz.l3x1.apiFace;

public class Main {

    public static void main(String[] args) {

        //Main pour DetectFace
        /*String detectOnImage = "age,gender,headPose,smile,facialHair,glasses,emotion,hair,makeup,occlusion,accessories,blur,exposure,noise";

        String path = "src/main/resources/hocine.jpg";

        HttpEntity entity = DetectFace.requete(path, detectOnImage);

        String jsonString = Json.httpToString(entity);

        System.out.println(jsonString);
         //Création d'un fichier à partir du nom de fichier
        //Json.jsonToFile(jsonString, path);
        */

        //Main pour FaceList
        //FaceList.create("l", "l", "y");

        //FaceList.getFaceList();

        //FaceList.addFace("src/main/resources/yoni1.jpg","l","yoni1");

        //FaceList.getFaceOflist("l");


    }
}
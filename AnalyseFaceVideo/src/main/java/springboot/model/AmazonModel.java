package springboot.model;

import java.util.List;

public class AmazonModel {
     private static List<Persons> photos;

     public AmazonModel()
     {
          // instnaci le model
     }

     public static List<Persons> getPhotos() {
          return photos;
     }

     public void addPerson(Persons photos)
     {
          AmazonModel.photos.add(photos);
     }
}

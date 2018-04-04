package springboot.model;

import java.util.List;

public class AmazonModel {
     public static List<Persons> photos;

     public AmazonModel()
     {
          // instnaci le model
     }

     public void addPerson(Persons photos)
     {
          AmazonModel.photos.add(photos);
     }
}

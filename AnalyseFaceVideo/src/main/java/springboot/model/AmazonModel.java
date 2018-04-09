package springboot.model;

import java.util.ArrayList;
import java.util.List;

public class AmazonModel {
     private  List<Persons> photos;

     public AmazonModel()
     {
         photos = new ArrayList<>();
     }

     public  List<Persons> getPhotos() {
          return this.photos;
     }

     public void addPerson(Persons photos)
     {
          this.photos.add(photos);
     }
}

package SpringBoot.model;

import java.util.ArrayList;
import java.util.List;

public class MicrosoftModel {

    private List<Persons> photos;

    public MicrosoftModel() {
        photos = new ArrayList<Persons>();
    }

    public List<Persons> getPhotos() {
        return photos;
    }

    public void setPerson(List<Persons> photos) {
        this.photos = photos;
    }

    public void addPerson(Persons photos)
    {
        this.photos.add(photos);
    }


}

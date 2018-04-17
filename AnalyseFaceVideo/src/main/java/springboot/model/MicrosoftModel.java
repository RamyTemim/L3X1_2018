package springboot.model;

import java.util.ArrayList;
import java.util.List;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE model
 * <p>
 * Cette classe contient le modèle du modèle MVC pour Microsoft
 */
public class MicrosoftModel {

    private List<Persons> photos;

    public MicrosoftModel() {
        photos = new ArrayList<>();
    }

    /**
     * Méthode pour récupérer la liste des Persons
     *
     * @return Une liste contenant des objets Persons
     */
    public List<Persons> getPhotos() {
        return photos;
    }

    /**
     * Méthode permettant de rajouter un objet Persons à la liste
     *
     * @param photos L'objet Persons à rajouter dans la liste
     */
    public void addPerson(Persons photos) {
        this.photos.add(photos);
    }


}

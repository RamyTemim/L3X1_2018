package springboot.model;

import java.util.List;

/**
 * L3X1 FACIAL RECONGITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on vidéo)
 * <p>
 * PACKAGE model
 * <p>
 * Cette classe est le modèle du MVC contenant la liste des photos pour une vidéo
 */
public class Persons {
    private String name;
    private List<String> videos;

    public Persons() {
    }

    /**
     * Pour définir le nom de la personne auxquel on va lui associer les vidéos où il apparait
     *
     * @param name Nom de la personne
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Pour définir la liste des vidéos correspondant à la personne "name"
     *
     * @param videos La liste des vidéos
     */
    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    /**
     * Pour obtenir le nom de la personne
     *
     * @return Nom de la personne
     */
    public String getName() {
        return name;
    }

    /**
     * Pour obtenir la liste des vidéos ou la personne apparait
     *
     * @return La liste des vidéos
     */
    public List<String> getVideos() {
        return videos;
    }
}

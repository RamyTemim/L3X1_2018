package springboot.model;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE model
 * <p>
 * Cette classe contient le modèle du modèle MVC pour les listes
 * ou seront stocker les liens vers les photos et les vieos
 */

@Component
public class InputResources {

    private List<String> listpathTophoto;
    private List<String> listpathToVideo;

    /**
     * Getter pour récupérer la liste des liens vers les photos
     * @return listpathTophoto liste des liens
     */
    public List<String> getListpathTophoto()
    {
        return listpathTophoto;
    }

    /**
     * Setter pour replire la liste listpathTophoto de avec les liens vers les photos
     * @param listpathTophoto liste des liens
     */
    public void setListpathTophoto(List<String> listpathTophoto)
    {

        this.listpathTophoto = listpathTophoto;
    }


    /**
     * Getter pour récupérer la liste des liens vers les videos
     * @return listpathTovideo liste des liens
     */
    public List<String> getListpathToVideo()
    {
        return listpathToVideo;
    }

    /**
     * Setter pour replire la liste listpathTophoto de avec les liens vers les videos
     * @param listpathToVideo liste des liens
     */
    public void setListpathToVideo(List<String> listpathToVideo)
    {
        this.listpathToVideo = listpathToVideo;
    }
}

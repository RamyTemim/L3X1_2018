package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springboot.model.AmazonModel;
import springboot.model.InputResources;
import springboot.model.MicrosoftModel;
import springboot.service.AmazonServices;
import springboot.service.MicrosoftService;
import useful.Utils;

import java.io.File;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE controller
 * <p>
 * Cette classe contient le Controller de l'application ce qui va permettre l'interaction entre les modèle et la vue
 * dans le modèle MVC
 */
@RestController
@RequestMapping("/")
public class Controller {

    private final MicrosoftService microsoftService;

    private final InputResources inputResources;

    private final AmazonServices amazonService;

    @Autowired
    public Controller(MicrosoftService microsoftService, InputResources inputResources, AmazonServices amazonService) {
        this.microsoftService = microsoftService;
        this.inputResources = inputResources;
        this.amazonService = amazonService;
    }


    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien
     * pour le fichier contenant les photos
     *
     * @param multipartFileImage le fichier reçu du client avec le post
     */
    @RequestMapping(value = "/photos", method = RequestMethod.POST)
    public void postPathPhotos(@RequestParam("filePhoto") MultipartFile multipartFileImage) {
        File file = Utils.storeFile(multipartFileImage, "photo");
        inputResources.setListpathTophoto(Utils.readFile(file));
        Utils.checkLink(inputResources.getListpathTophoto(),"photo");
    }

    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien
     * pour le fichier contenant les vidéos
     *
     * @param multipartFileVideo le fichier reçu du client avec le post
     */
    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public void postPathVideos(@RequestParam("fileVideo") MultipartFile multipartFileVideo) {
        File file = Utils.storeFile(multipartFileVideo,"video" );
        inputResources.setListpathToVideo(Utils.readFile(file));
        Utils.checkLink(inputResources.getListpathToVideo(),"video");
    }

    /**
     * Méthode pour définir le get qui va permettre de récupérer le resultat
     * de l'analyse avec l'api de Amazon
     *
     * @return Le fichier json sous forme de String contenant le résultat de l'analyse de Amazon
     */
    @RequestMapping(value = "/amazon", method = RequestMethod.GET, produces = "application/json")
    public AmazonModel getAmazon() {
        Utils.sleepGet();
        return amazonService.getJson(inputResources.getListpathTophoto(), inputResources.getListpathToVideo());
    }

    /**
     * Méthode pour définir le get qui va permettre de récupérer le resultat
     * de l'analyse avec l'api de Microsoft
     *
     * @return Le fichier json sous forme de String contenant le résultat de l'analyse de Microsoft
     */
    @RequestMapping(value = "/microsoft", method = RequestMethod.GET, produces = "application/json")
    public MicrosoftModel getMicrosoft() {
        Utils.sleepGet();
        return microsoftService.getJson(inputResources.getListpathTophoto(), inputResources.getListpathToVideo());
    }

}



package springboot.controler;

import springboot.model.AmazonModel;
import springboot.model.MicrosoftModel;
import springboot.service.AmazonServices;
import springboot.service.MicrosoftService;
import microsoft.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    private MicrosoftService microsoftService;

    @Autowired
    private AmazonServices amazonServices;

    private List<String> listpathTophoto;
    private List<String> listpathToVideo;


    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien
     * pour le fichier contenant les photos
     * @param multipartFileImage le fichier reçu du client avec le post Angular
     */
    @RequestMapping(value = "/photos", method = RequestMethod.POST)
    public void postPathPhotos(@RequestParam("filePhoto") MultipartFile multipartFileImage) throws IOException {
        File file = JsonUtil.storeFilePhoto(multipartFileImage);
        this.listpathTophoto=JsonUtil.readFile(file);

    }

    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien
     * pour le fichier contenant les vidéos
     * @param multipartFileVideo le fichier reçu du client avec le post Angular
     */
    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public void postPathVideos (@RequestParam("fileVideo") MultipartFile multipartFileVideo) throws IOException {
        File file =JsonUtil.storeFileVideo(multipartFileVideo);
        this.listpathToVideo=JsonUtil.readFile(file);

    }


   @RequestMapping(value ="/amazon", method = RequestMethod.GET, produces ="application/json")
    public AmazonModel getAmazon() throws InterruptedException, IOException {
        JsonUtil.sleepGet();
        return amazonServices.getJson(this.listpathTophoto, this.listpathToVideo);
   }



    /**
     * Méthode pour définir le get qui va permettre de récupérer le resultat
     * de l'analyse avec l'api de Microsoft
     * @return Le fichier json sous forme de String contenant le résultat de l'analyse de Microsoft
     */
    @RequestMapping(value ="/microsoft", method = RequestMethod.GET, produces ="application/json")
    public MicrosoftModel getMicrosoft() throws IOException, InterruptedException {
        JsonUtil.sleepGet();
        return microsoftService.getJson(this.listpathTophoto, this.listpathToVideo);
    }


}



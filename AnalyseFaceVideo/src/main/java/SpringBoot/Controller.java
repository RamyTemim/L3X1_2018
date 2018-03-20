package SpringBoot;

import microsoft.JsonUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;


@RestController
@RequestMapping("/")
public class Controller {

    @Autowired
    private MicrosoftService microsoftService;

    @Autowired
    private AmazonServices amazonServices;

    @Autowired
    private  StorageService storageService;

    private List<String> listpathTophoto;
    private List<String> listpathToVideo;

    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien pour le fichier contenant les photos
     *
     * @param multipartFileImage le fichier reçu du client avec le post Angular
     */
    @RequestMapping(value = "/photos", method = RequestMethod.POST)
    public void PostPathPhotos(@RequestParam("file") MultipartFile multipartFileImage) {
        try {

            storageService.storFile(multipartFileImage);
            File  listePhoto =new File("src/main/resources/"+multipartFileImage.getOriginalFilename());
            listpathTophoto=JsonUtil.readFile(listePhoto);
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien pour le fichier contenant les vidéos
     * @param multipartFileVideo le fichier reçu du client avec le post Angular
     */
    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public void PostPathVideos (@RequestParam("file") MultipartFile multipartFileVideo)
    {

        try {
            storageService.storFile(multipartFileVideo);
            File  listeVideo =new File("src/main/resources/"+multipartFileVideo.getOriginalFilename());
            listpathToVideo=JsonUtil.readFile(listeVideo);

        } catch (Exception e) {
            System.err.println("le fichier qui contient les path vers les vidéos n'a pa pu etre lu");
        }
    }



    /**
     * Méthode pour définir le get qui va permettre de récupérer le resultat de l'analyse avec l'api de Amazon
     * @return Le fichier json sous forme de String contenant le résultat de l'analyse de amazon
     */
    @RequestMapping(value ="/amazon", method = RequestMethod.GET, produces ="application/json")
    public String getAmazon()
    {
        String resultOfAnalyseAmazon;
        resultOfAnalyseAmazon = amazonServices.getJson(this.listpathTophoto, this.listpathToVideo);
        JSONObject json = new JSONObject();
        json.put("Amazon ",new JSONObject(resultOfAnalyseAmazon ) );
        return  json.toString() ;
    }



    /**
     * Méthode pour définir le get qui va permettre de récupérer le resultat de l'analyse avec l'api de Microsoft
     * @return Le fichier json sous forme de String contenant le résultat de l'analyse de Microsoft
     */
    @RequestMapping(value ="/microsoft", method = RequestMethod.GET, produces ="application/json")
    public String getMicrosoft()
    {
        System.out.println(this.listpathTophoto );
        System.out.println(this.listpathToVideo );
        String resultOfAnalyseMicrosoft;
        resultOfAnalyseMicrosoft=microsoftService.getJson(this.listpathTophoto, this.listpathToVideo);
        JSONObject json = new JSONObject();
        json.put("Microsoft ",new JSONObject(resultOfAnalyseMicrosoft ));

        return  json.toString() ;
    }
}


package SpringBoot.controler;

import SpringBoot.service.MicrosoftService;
import microsoft.JsonUtil;
import org.json.JSONObject;
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

    private List<String> listpathTophoto;
    private List<String> listpathToVideo;


    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien pour le fichier contenant les photos
     * @param multipartFileImage le fichier reçu du client avec le post Angular
     */
    @RequestMapping(value = "/photos", method = RequestMethod.POST)
    public void postPathPhotos(@RequestParam("filePhoto") MultipartFile multipartFileImage)
    {
        File file = JsonUtil.storeFilePhoto(multipartFileImage);
        this.listpathTophoto=JsonUtil.readFile(file);

    }

    /**
     * Méthode pour définir le Post dans l'application qui va récupérer le lien pour le fichier contenant les vidéos
     * @param multipartFileVideo le fichier reçu du client avec le post Angular
     */
    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public void postPathVideos (@RequestParam("fileVideo") MultipartFile multipartFileVideo)
    {
        File file =JsonUtil.storeFileVideo(multipartFileVideo);
        this.listpathToVideo=JsonUtil.readFile(file);

    }

    /*
     * Méthode pour définir le get qui va permettre de récupérer le resultat de l'analyse avec l'api de Amazon
     * @return Le fichier json sous forme de String contenant le résultat de l'analyse de amazon
     */
/*    @RequestMapping(value ="/amazon", method = RequestMethod.GET, produces ="application/json")
    public String getAmazon()
    {
        String resultOfAnalyseAmazon;
        resultOfAnalyseAmazon = amazonServices.getJson(this.listpathTophoto, this.listpathToVideo);
        JSONObject json = new JSONObject();
        json.put("Amazon ",new JSONObject(resultOfAnalyseAmazon ) );
        return  json.toString() ;
    }
*/


    /**
     * Méthode pour définir le get qui va permettre de récupérer le resultat de l'analyse avec l'api de Microsoft
     * @return Le fichier json sous forme de String contenant le résultat de l'analyse de Microsoft
     */
    @RequestMapping(value ="/microsoft", method = RequestMethod.GET, produces ="application/json")

    public String getMicrosoft() throws IOException
    {

    
        System.out.println("Attente de Prise en compte du POST");

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Erreur dans le sleep : ");
        }
        System.out.println("Fin Attente");


        String resultOfAnalyseMicrosoft;
        resultOfAnalyseMicrosoft=microsoftService.getJson(this.listpathTophoto, this.listpathToVideo);
        JSONObject json = new JSONObject();
        json.put("Microsoft ",new JSONObject(resultOfAnalyseMicrosoft ));


        return  json.toString() ;
    }


    @RequestMapping(value ="/test", method = RequestMethod.GET, produces ="application/json")
    public String getTest(){

        return  new JSONObject("{\n" +
                "    \"Microsoft \": {\n" +
                "        \"julien-smadja\": [\n" +
                "            \"XebiCon-17-Les-assistants-du-futur-2022-cest-déjà-demain-2\",\n" +
                "            \"XebiCon-17-Les-assistants-du-futur-2022-cest-déjà-demain\",\n" +
                "            \"XebiaCon17_Les_assistants_du_futur_2022_cest_déjà_demain-HD\"\n" +
                "        ],\n" +
                "        \"renaud_chevalier\": [\n" +
                "            \"XebiCon_17_Comment_identifier_ses_tribus_en_4_étapes-HD\",\n" +
                "            \"XebiCon-17-Comment-identifier-ses-tribus-en-4-étapes\"\n" +
                "        ],\n" +
                "        \"sameh\": [],\n" +
                "        \"mael\": [],\n" +
                "        \"akram_blouza\": [\n" +
                "            \"XebiCon-17-Migration-d-une-application-web-vers-un-PaaS-Openshift\",\n" +
                "            \"XebiCon17_Migration_une_application_web_vers_un_PaaS_Openshift_2-HD\"\n" +
                "        ],\n" +
                "        \"arnaud_Brachetti\": [\n" +
                "            \"XebiCon_17_Une longue_route_vers_la_transformation_Agile_de_lentreprise_1-HD\",\n" +
                "            \"XebiCon-17-Une-longue-route-vers-la-transformation-Agile-de-lentreprise\"\n" +
                "        ]\n" +
                "    }\n" +
                "}").toString() ;
    }


}



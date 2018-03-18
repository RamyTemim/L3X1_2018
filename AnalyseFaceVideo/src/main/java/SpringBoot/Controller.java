package SpringBoot;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class Controller
{

    @Autowired
    private MicrosoftService microsoftService;

    @Autowired
    private AmazonServices amazonServices;

    private String pathPhoto = null;
    private String pathVideo = null;

    @RequestMapping(value = "/photos", method = RequestMethod.POST)
    public void PostPathPhotos (@RequestBody String pathPhoto)
    {
        System.out.println(pathPhoto);
        JSONObject pathPhotosJson = new JSONObject(pathPhoto);
        this.pathPhoto = pathPhotosJson.get("name").toString();
    }

    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public void PostPathVideos (@RequestBody String pathVideo)
    {
        System.out.println(pathVideo);
        JSONObject pathVideoJson = new JSONObject(pathVideo);
        this.pathVideo = pathVideoJson.get("name").toString();
    }

    @RequestMapping(value ="/amazon", method = RequestMethod.GET, produces ="application/json")
    public String getAmazon() throws Exception
    {
        String resultOfAnalyseAmazon;
        resultOfAnalyseAmazon = amazonServices.getJson(this.pathPhoto, this.pathVideo);
        JSONObject json = new JSONObject();
        json.put("Amazon ",new JSONObject(resultOfAnalyseAmazon ) );
        return  json.toString() ;
    }

    @RequestMapping(value ="/microsoft", method = RequestMethod.GET, produces ="application/json")
    public String getMicrosoft() throws Exception
    {
        System.out.println(this.pathPhoto );
        System.out.println(this.pathVideo );
        String resultOfAnalyseMicrosoft;
        resultOfAnalyseMicrosoft=microsoftService.getJson(this.pathPhoto, this.pathVideo);
        JSONObject json = new JSONObject();
        json.put("Microsoft ",new JSONObject(resultOfAnalyseMicrosoft ));

        return  json.toString() ;
    }
}
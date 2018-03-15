package SpringBoot;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static SpringBoot.AmazonServices.*;

@RestController
@RequestMapping("/")
public class Controller
{

    @Autowired
    private MicrosoftService microsoftService;

    @RequestMapping(value ="/", method = RequestMethod.GET, produces ="application/json")
    public String getJSONObjectAmazon() throws Exception {

       AmazonServices.readfileOfpath();

       String resultOfAnalyseAmazon;
       resultOfAnalyseAmazon = getJsonObjectAmazon();
       String resultOfAnalyseMicrosoft;
       resultOfAnalyseMicrosoft=microsoftService.getJSON();

       JSONObject finalResutJson = new JSONObject();
       finalResutJson.put("Amazon ",new JSONObject(resultOfAnalyseAmazon ) );
       finalResutJson.put("Microsoft ",new JSONObject(resultOfAnalyseMicrosoft ));

       return  finalResutJson.toString() ;
    }
}
package SpringBoot;


import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static SpringBoot.AmazonServices.*;

@RestController
@RequestMapping("/")
public class Controleur
{

    @Autowired
    private MicrosoftService microsoftService;

    @RequestMapping(value ="/", method = RequestMethod.GET, produces ="application/json")

    public String getJSONObjectAmazon() throws Exception {

       String resultOfAnalyseAmazon;
       resultOfAnalyseAmazon = getJsonObjectAmazon();
       String resultOfAnalyseMicrosoft;
       resultOfAnalyseMicrosoft=microsoftService.getJSON();

       JSONObject re = new JSONObject();
       re.put("Amazon ",new JSONObject(resultOfAnalyseAmazon ) );
       re.put("Microsoft ",new JSONArray(resultOfAnalyseMicrosoft ));

       return  re.toString() ;
    }
}
package SpringBoot;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import microsoft.*;
import java.io.IOException;

import static SpringBoot.AmazonService.*;

@RestController
@RequestMapping("/")
public class AmazonControler
{

    @Autowired
    private AmazonService microsoftService;


    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String getJSONObjectAmazon() throws Exception {

        String resultOfAnalyseAmazon;
       resultOfAnalyseAmazon = getJsonObjectAmazon();
         String resultOfAnalyseMicrosoft;
        resultOfAnalyseMicrosoft= microsoftService.getJSON();

        JSONObject re = new JSONObject();
        JSONArray ra = new JSONArray();

        ra.put(resultOfAnalyseAmazon);
        ra.put(resultOfAnalyseMicrosoft);

        re.append("Resulat", ra);
        return re.toString(2);
    }



/*
    @Autowired
    private AmazonService microsoftService;

    @RequestMapping(value="/", method = RequestMethod.GET, produces = "application/json")
    public String getJSONObjectM () throws IOException {
        return microsoftService.getJSON();
    }*/
}
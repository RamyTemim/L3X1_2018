package SpringBoot;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AmazonControler
{
  // private AmazonService amazonService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String getJSONObjectAmazon() throws Exception {
        AmazonService  amazonService =new AmazonService();
        String resultOfAnalyse;
       resultOfAnalyse = amazonService.getJsonObjectAmazon();

        return resultOfAnalyse;
    }
}
package SpringBoot;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static SpringBoot.AmazonService.*;

@RestController
@RequestMapping("/")
public class AmazonControler
{

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String getJSONObjectAmazon() throws Exception {

        String resultOfAnalyse;
       resultOfAnalyse = getJsonObjectAmazon();
        return resultOfAnalyse + "\n";
    }
}
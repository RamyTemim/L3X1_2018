package yoniz.l3x1.springBoot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import yoniz.l3x1.springBoot.service.MicrosoftService;

import java.io.IOException;

@RestController
@RequestMapping("/microsoft")
public class MicrosoftController {
    @Autowired
    private MicrosoftService microsoftService;

    @RequestMapping(value="/json", method = RequestMethod.GET, produces = "application/json")
    public String getJSONObject () throws IOException {
        return microsoftService.getJSON();
    }
}

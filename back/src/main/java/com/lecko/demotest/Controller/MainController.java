package com.lecko.demotest.Controller;

import com.lecko.demotest.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/mails")
@EntityScan("com.microsoft.graph.serializer")
public class MainController {
    @Autowired
    MailService mailService;

    @RequestMapping(
            value = "/MailsPerMonth",
            method = GET)
    public String countMailsPerMonth() {
        return mailService.countMailsPerMonth();
    }

    @RequestMapping(
            value = "/all",
            method = GET)
    @ResponseBody
    public String getFooAsJsonFromBrowser() {
        return mailService.getAllMails();
    }
}

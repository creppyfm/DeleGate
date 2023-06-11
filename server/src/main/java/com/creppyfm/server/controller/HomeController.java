package com.creppyfm.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@CrossOrigin
@RestController
@AllArgsConstructor
@Tag(name = "Home Controller")
public class HomeController {

    @Autowired
    MongoOperations mongoOperations;
/*
    This is in place to redirect the user back to the frontend after successful
    login.
    This should be removed from production build.
*/
    @GetMapping("/")
    public RedirectView redirectToExternalUrl() {

        String externalUrl = "https://creppyfm.github.io/DeleGate/";
        return new RedirectView(externalUrl);
    }
}

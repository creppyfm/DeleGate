package com.creppyfm.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class HomeController {

/*
    This is in place to redirect the user back to the frontend after successful
    login.
    This should be removed from production build.
*/
    @GetMapping("/")
    public RedirectView redirectToExternalUrl() {
        String externalUrl = "http://localhost:5173";
        return new RedirectView(externalUrl);
    }
}

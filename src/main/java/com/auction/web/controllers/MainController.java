package com.auction.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping("/principal")
    public @ResponseBody
    Principal user(Principal user) {
        return user;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "/pages/index.html";
    }

    @RequestMapping(value = "auth", method = RequestMethod.GET)
    public String login() {
        return "/pages/login.html";
    }

    @RequestMapping(value = "session", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String invalidSession() {
        return "/pages/login.html";
    }

    @RequestMapping(value = "/{[path:[^\\.]*}")
    public String redirect() {
        return "forward:/";
    }
}

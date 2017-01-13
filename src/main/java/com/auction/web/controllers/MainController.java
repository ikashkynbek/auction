package com.auction.web.controllers;

import com.auction.model.Role;
import com.auction.model.User;
import com.auction.service.MainService;
import com.auction.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    private MainService service;

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


    @RequestMapping(value="/userRegister", method = RequestMethod.GET)
    public String register() {
        return "/pages/userregister.html";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public @ResponseBody User addUser(HttpServletRequest request, HttpServletResponse response){

        String userName = request.getParameter("username");
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        Long userId = service.createUser(login, Utils.getPwdHash(password, login), userName, Role.CUSTOMER);
        User user = new User();
        user.setId(userId);
        user.setName(userName);
        user.setLogin(login);
        user.setRole(Role.CUSTOMER);
        return user;
    }

}

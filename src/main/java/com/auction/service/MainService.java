package com.auction.service;

import com.auction.model.Role;
import com.auction.model.User;

public interface MainService {

    Long createUser(String login, String password, String name, Role role);

    User getUser(String login);

}

package com.auction.web.rest;

import com.auction.model.DefaultResponse;
import com.auction.model.Order;
import com.auction.model.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping(value = "/orders")
public class RestOrders extends BaseRest {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void branchList(HttpServletResponse response) {
        try {
            List<Order> orders = new ArrayList<>();
            Role role = getRole();
            if (role.equals(Role.ADMIN)) {
                orders = auctionService.listOrders();
            }
            returnResponse(response, new DefaultResponse(orders));
        } catch (Exception ex) {
            renderError(response, ex);
        }
    }
}


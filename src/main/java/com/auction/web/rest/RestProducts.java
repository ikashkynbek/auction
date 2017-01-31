package com.auction.web.rest;

import com.auction.model.DefaultResponse;
import com.auction.model.Order;
import com.auction.model.Product;
import com.auction.model.Role;
import com.auction.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansharky on 1/16/17.
 */
@RestController
@RequestMapping(value = "/products")
public class RestProducts extends BaseRest{

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void listProducts(HttpServletResponse response) {
        try {
            List<Product> products = new ArrayList<>();
            Role role = getRole();
            if (role.equals(Role.ADMIN)) {
                products = productService.listProducts();
            }
            returnResponse(response, new DefaultResponse(products));
        } catch (Exception ex) {
            renderError(response, ex);
        }
    }

    @RequestMapping(value = "/newProduct", method = RequestMethod.POST)
    public void createNewProduct(@RequestBody Product product, HttpServletResponse response) {
        try {
            productService.createProduct(product);
            returnResponse(response, new DefaultResponse(product));
        } catch (Exception ex) {
            renderError(response, ex);
        }
    }
}

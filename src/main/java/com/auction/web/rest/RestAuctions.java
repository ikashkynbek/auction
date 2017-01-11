package com.auction.web.rest;

import com.auction.model.Auction;
import com.auction.model.DefaultResponse;
import com.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by dansharky on 1/11/17.
 */

@RestController
@RequestMapping(value = "/auctions")
public class RestAuctions extends BaseRest{

    @Autowired
    private AuctionService auctionService;

    @RequestMapping(value = "/newAuction", method = RequestMethod.POST)
    public void createNewAuction(@RequestBody Auction auction, HttpServletResponse response) {
        try {
            auctionService.createAuction(auction);
            returnResponse(response, new DefaultResponse(auction));
        } catch (Exception ex) {
            renderError(response, ex);
        }
    }
}

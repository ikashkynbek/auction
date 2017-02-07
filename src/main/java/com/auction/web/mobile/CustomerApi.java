package com.auction.web.mobile;

import com.auction.model.DefaultResponse;
import com.auction.web.rest.BaseRest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api")
public class CustomerApi extends BaseRest {

    @RequestMapping(value = "/auctions", method = RequestMethod.GET)
    public void branchList(HttpServletResponse response) {
        try {
            List<Map<String, Object>> auctions = auctionService.auctionDetails();
            returnResponse(response, new DefaultResponse(auctions));
        } catch (Exception ex) {
            renderError(response, ex);
        }
    }
}


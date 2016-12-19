package com.auction.web.socket;

import com.auction.model.Auction;
import com.auction.model.Quote;
import com.auction.service.AuctionService;
import com.auction.utils.MatchingService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;


@Controller
public class AuctionController {

    private static final Logger log = Logger.getLogger(AuctionController.class);

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private MatchingService matchingService;

    @SubscribeMapping("/auctions")
    public List<Auction> getAuctions(Principal principal) throws Exception {
        log.info("Auctions for " + principal.getName());
        List<Auction> auctions = auctionService.listAuctions();
        auctions.forEach(a -> a.setQuotes(auctionService.listQuotesGroup(a.getId())));
        return auctions;
    }

    @MessageMapping("/quote")
    public void executeQuote(Quote quote, Principal principal) {
        quote.setOwner(principal.getName());
        quote.setLeavesQty(quote.getQty());
        System.out.println(quote);
        matchingService.matching(quote);
    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}

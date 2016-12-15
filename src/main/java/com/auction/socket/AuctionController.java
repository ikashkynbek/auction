package com.auction.socket;

import com.auction.model.Auction;
import com.auction.service.AuctionService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
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

    @SubscribeMapping("/auctions")
    public List<Auction> getAuctions(Principal principal) throws Exception {
        log.info("Auctions for " + principal.getName());
        return auctionService.listAuctions();
    }

//    @MessageMapping("/trade")
//    public void executeTrade(Trade trade, Principal principal) {
//        trade.setUsername(principal.getName());
//        System.out.println("Trade: " + trade);
//        this.tradeService.executeTrade(trade);
//    }

    @MessageExceptionHandler
    @SendToUser("/topic/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

}

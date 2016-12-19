package com.auction.utils;

import com.auction.model.Order;
import com.auction.model.Quote;
import com.auction.model.QuoteMsg;
import com.auction.model.QuoteType;
import com.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.auction.model.QuoteStatus.FILLED;
import static com.auction.model.QuoteStatus.PARTIALLY_FILLED;


@Component
public class MatchingService {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private MessageSendingOperations<String> messaging;

    public void matching(Quote newQuote) {

        Long newQuoteId = auctionService.createQuote(newQuote);
        sendMsg(QuoteMsg.Action.ADD, newQuote.getQty(), newQuote);
        List<Quote> quotes = auctionService.matchingQuotes(newQuote.getAuctionId(), newQuote.getType(), newQuote.getPrice());

        for (Quote oldQuote : quotes) {
            String customer, merchant;
            int leavesQtyOld, leavesQtyNew;
            if (newQuote.getType().equals(QuoteType.BID)) {
                customer = newQuote.getOwner();
                merchant = oldQuote.getOwner();
                leavesQtyNew = 0;
                leavesQtyOld = oldQuote.getLeavesQty() - newQuote.getLeavesQty();
            } else {
                customer = oldQuote.getOwner();
                merchant = newQuote.getOwner();
                leavesQtyNew = newQuote.getLeavesQty() - oldQuote.getLeavesQty();
                leavesQtyOld = 0;
            }

            Order order = new Order();
            order.setAuctionId(oldQuote.getAuctionId());
            order.setCustomer(customer);
            order.setMerchant(merchant);
            order.setPrice(oldQuote.getPrice());
            auctionService.createOrder(order);

            auctionService.updateQuote(oldQuote.getId(), leavesQtyOld, leavesQtyOld > 0 ? PARTIALLY_FILLED : FILLED);
            auctionService.updateQuote(newQuoteId, leavesQtyNew, leavesQtyNew > 0 ? PARTIALLY_FILLED : FILLED);
            newQuote.setLeavesQty(leavesQtyNew);
            sendMsg(QuoteMsg.Action.REMOVE, 1, oldQuote);
            sendMsg(QuoteMsg.Action.REMOVE, 1, newQuote);

            if (leavesQtyNew == 0) break;
        }
    }

    private void sendMsg (QuoteMsg.Action action, Integer qty, Quote q) {
        QuoteMsg msg = new QuoteMsg();
        msg.setAction(action);
        msg.setAuctionId(q.getAuctionId());
        msg.setQty(qty);
        msg.setPrice(q.getPrice());
        msg.setType(q.getType());
        messaging.convertAndSend("/topic/quote", msg);
    }
}

package com.auction.utils;

import com.auction.model.Order;
import com.auction.model.Quote;
import com.auction.model.QuoteType;
import com.auction.service.OrderService;
import com.auction.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.auction.model.QuoteStatus.FILLED;
import static com.auction.model.QuoteStatus.PARTIALLY_FILLED;


@Component
public class MatchingService {

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private OrderService orderService;

    public void matching(Quote newQuote) {

        Long newQuoteId = quoteService.createQuote(newQuote);
        List<Quote> quotes = quoteService.matchingQuotes(newQuote.getAuctionId(), newQuote.getType(), newQuote.getPrice());

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
            orderService.createOrder(order);

            quoteService.updateQuote(oldQuote.getId(), leavesQtyOld, leavesQtyOld > 0 ? PARTIALLY_FILLED : FILLED);
            quoteService.updateQuote(newQuoteId, leavesQtyNew, leavesQtyNew > 0 ? PARTIALLY_FILLED : FILLED);
            newQuote.setLeavesQty(leavesQtyNew);

            if (leavesQtyNew == 0) break;
        }
    }
}

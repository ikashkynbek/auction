package com.auction.service;

import com.auction.model.Quote;
import com.auction.model.QuoteStatus;
import com.auction.model.QuoteType;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface QuoteService {

    List<Quote> listQuotes(Long auctionId);

    List<Quote> listQuotesGroup(Long auctionId);

    List<Quote> matchingQuotes(Long auctionId, QuoteType type, Double price);

    Long createQuote(Quote quote);

    void updateQuote(Long id, Integer leavesQty, QuoteStatus status);

    void deleteQuote(Long id);
}

package com.auction.utils;

import com.auction.model.Auction;
import com.auction.model.Quote;
import com.auction.model.QuoteType;
import com.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@EnableScheduling
public class Generator {

    private static String[] merchants = {"sulpak", "tehnodom", "alser"};
    private static String[] customers = {"islam", "max", "andrey", "vitaliy"};

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private MatchingService matchingService;

    @PostConstruct
    public void generateAuctions() {
        List<Auction> auctions = auctionService.listAuctions();
        if (auctions.isEmpty()) {
            Auction a1 = new Auction();
            a1.setProductName("LED LG 43LF630V Black");
            auctionService.createAuction(a1);

            Auction a2 = new Auction();
            a2.setProductName("Samsung WF8590NLW9DY White");
            auctionService.createAuction(a2);
        }
    }

    @Scheduled(fixedDelay=60000)
    public void generateQuotes() {
        for (Auction auction : auctionService.listAuctions()) {
            Quote quote = generateQuote(auction.getId());
            System.out.println(quote);
            matchingService.matching(quote);
        }
    }

    public Quote generateQuote(Long audionId) {
        Quote quote = new Quote();
        quote.setAuctionId(audionId);
        quote.setType(nextType());
        if (quote.getType().equals(QuoteType.OFFER)) {
            quote.setOwner(merchants[nextInt(0, merchants.length)]);
//            quote.setQty(nextInt(1, 10));
            quote.setQty(1);
            quote.setLeavesQty(quote.getQty());
        } else {
            quote.setOwner(customers[nextInt(0, customers.length)]);
            quote.setQty(1);
            quote.setLeavesQty(1);
        }
        quote.setPrice((double) nextInt(97000, 97010));
        return quote;
    }

    private static double nexDouble(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private static QuoteType nextType() {
        return QuoteType.values()[new Random().nextInt(QuoteType.values().length)];
    }
}
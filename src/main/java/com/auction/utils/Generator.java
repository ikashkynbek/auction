package com.auction.utils;

import com.auction.model.Auction;
import com.auction.model.AuctionStatus;
import com.auction.model.Quote;
import com.auction.model.QuoteType;
import com.auction.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.List;

@Component
@EnableScheduling
public class Generator {

    public static final SecureRandom RANDOM = new SecureRandom();
    public static final SecureRandom RANDOM2 = new SecureRandom();
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
            a1.setStatus(AuctionStatus.ACTIVE);
            auctionService.createAuction(a1);

            Auction a2 = new Auction();
            a2.setProductName("Samsung WF8590NLW9DY White");
            a2.setStatus(AuctionStatus.ACTIVE);
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
            quote.setQty(nextInt(1, 6));
            quote.setLeavesQty(quote.getQty());
        } else {
            quote.setOwner(customers[nextInt(0, customers.length)]);
            quote.setQty(1);
            quote.setLeavesQty(1);
        }
        quote.setPrice(nextDouble(90, 100));
        return quote;
    }

    private static int nextInt(int from, int to) {
        return (int) nextFloat(from, to);
    }

    private static double nextDouble(double from, double to) {
        return round(nextFloat(from, to));
    }

    private static double nextFloat(double from, double to) {
        if (to <= from) return -1;
        double mean = 1.0 * (to + from) / 2;
        double dev = (to - from) / 10;
        double val = RANDOM.nextGaussian() * dev + mean;
        if (val > to) val = to;
        if (val < from) val = from;
        return val;
    }

    private static QuoteType nextType() {
        if (RANDOM2.nextInt(10) > 6) { return QuoteType.OFFER; }
        else { return QuoteType.BID; }
    }

    private static double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

//    public static void main(String[] args) {
//        List<QuoteType> list = new ArrayList<>();
//        for (int i =0; i<=10; i++) {
//            list.add(nextType());
//        }
//        Collections.sort(list);
//        for (int i =0; i<=10; i++) {
//            System.out.println(list.get(i));
//        }
//    }
}
package com.auction.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Auction {

    private Long id;
    private String productName;
    private Date startDate;
    private Date endDate;
    private AuctionStatus status;
    private List<Quote> quotes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public AuctionStatus getStatus() { return status; }

    public void setStatus(AuctionStatus status) { this.status = status; }
}

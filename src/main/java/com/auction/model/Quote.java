package com.auction.model;

public class Quote {

    private Long id;
    private Long auctionId;
    private String owner;
    private Integer qty;
    private Integer leavesQty;
    private Double price;
    private QuoteType type;
    private QuoteStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(Long auctionId) {
        this.auctionId = auctionId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getLeavesQty() {
        return leavesQty;
    }

    public void setLeavesQty(Integer leavesQty) {
        this.leavesQty = leavesQty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public QuoteType getType() {
        return type;
    }

    public void setType(QuoteType type) {
        this.type = type;
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public void setStatus(QuoteStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "\n\tid: " + id +
                "\n\tauctionId: " + auctionId +
                "\n\towner: " + owner +
                "\n\tqty: " + qty +
                "\n\tleavesQty: " + leavesQty +
                "\n\tprice: " + price +
                "\n\ttype: " + type +
                "\n\tstatus: " + status;
    }
}

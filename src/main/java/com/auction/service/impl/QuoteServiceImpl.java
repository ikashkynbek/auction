package com.auction.service.impl;

import com.auction.model.Quote;
import com.auction.model.QuoteStatus;
import com.auction.model.QuoteType;
import com.auction.service.AbstractService;
import com.auction.service.QuoteService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class QuoteServiceImpl extends AbstractService implements QuoteService {

    @Override
    public List<Quote> listQuotes(Long auctionId) {
        return db.query("SELECT * FROM quotes WHERE auction_id=? AND status IN('CREATED', 'PARTIALLY_FILLED') ORDER BY price DESC",
                new QuoteMapper(), auctionId);
    }

    @Override
    public List<Quote> listQuotesGroup(Long auctionId) {
        return db.query("SELECT sum(leaves_qty) AS qty, price, type FROM quotes " +
                        "WHERE auction_id = ? AND status IN ('CREATED', 'PARTIALLY_FILLED') " +
                        "GROUP BY price, type ORDER BY price DESC",
                new QuoteGroupMapper(), auctionId);
    }

    @Override
    public List<Quote> matchingQuotes(Long auctionId, QuoteType type, Double price) {
        String query = "SELECT * FROM quotes WHERE auction_id=? AND leaves_qty > 0 AND status IN('CREATED', 'PARTIALLY_FILLED') ";
        if (type.equals(QuoteType.BID)) {
            query += "AND type='OFFER' AND price<=? ORDER BY price ASC";
        } else {
            query += "AND type='BID' AND price>=? ORDER BY price DESC";
        }
        return db.query(query, new QuoteMapper(), auctionId, price);
    }

    @Override
    public Long createQuote(Quote quote) {
        String insert = "INSERT INTO quotes(auction_id, owner, qty, leaves_qty, price, type, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setLong(1, quote.getAuctionId());
            ps.setString(2, quote.getOwner());
            ps.setInt(3, quote.getQty());
            ps.setInt(4, quote.getLeavesQty());
            ps.setDouble(5, quote.getPrice());
            ps.setString(6, quote.getType().name());
            ps.setString(7, QuoteStatus.CREATED.name());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public void updateQuote(Long id, Integer leavesQty, QuoteStatus status) {
        db.update("UPDATE quotes SET leaves_qty=?, status=? WHERE id = ?", leavesQty, status.name(), id);
    }

    @Override
    public void deleteQuote(Long id) {
        db.update("DELETE FROM quotes WHERE id = ?", id);
    }

    private static class QuoteMapper implements RowMapper<Quote> {
        @Override
        public Quote mapRow(ResultSet rs, int i) throws SQLException {
            Quote quote = new Quote();
            quote.setId(rs.getLong("id"));
            quote.setAuctionId(rs.getLong("auction_id"));
            quote.setOwner(rs.getString("owner"));
            quote.setQty(rs.getInt("qty"));
            quote.setLeavesQty(rs.getInt("leaves_qty"));
            quote.setPrice(rs.getDouble("price"));
            quote.setType(QuoteType.valueOf(rs.getString("type")));
            quote.setStatus(QuoteStatus.valueOf(rs.getString("status")));
            return quote;
        }
    }

    private static class QuoteGroupMapper implements RowMapper<Quote> {
        @Override
        public Quote mapRow(ResultSet rs, int i) throws SQLException {
            Quote quote = new Quote();
            quote.setQty(rs.getInt("qty"));
            quote.setPrice(rs.getDouble("price"));
            quote.setType(QuoteType.valueOf(rs.getString("type")));
            return quote;
        }
    }
}

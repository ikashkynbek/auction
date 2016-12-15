package com.auction.service.impl;

import com.auction.model.Auction;
import com.auction.service.AbstractService;
import com.auction.service.AuctionService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class AuctionServiceImpl extends AbstractService implements AuctionService {

    @Override
    public List<Auction> listAuctions() {
        return db.query("SELECT * FROM auctions", new AuctionMapper());
    }

    @Override
    public Auction getAuction(String productName) {
        List<Auction> auctions = db.query("SELECT * FROM auctions WHERE product_name=?", new AuctionMapper(), productName);
        return auctions != null && !auctions.isEmpty() ? auctions.get(0) : null;
    }

    @Override
    public Long createAuction(Auction auction) {
        String insert = "INSERT INTO auctions(product_name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, auction.getProductName());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private static class AuctionMapper implements RowMapper<Auction> {
        @Override
        public Auction mapRow(ResultSet rs, int i) throws SQLException {
            Auction auction = new Auction();
            auction.setId(rs.getLong("id"));
            auction.setProductName(rs.getString("product_name"));
            return auction;
        }
    }
}

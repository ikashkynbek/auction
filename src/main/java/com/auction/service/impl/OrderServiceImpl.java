package com.auction.service.impl;

import com.auction.model.Order;
import com.auction.service.AbstractService;
import com.auction.service.OrderService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class OrderServiceImpl extends AbstractService implements OrderService {

    @Override
    public List<Order> listOrders() {
        return db.query("SELECT * FROM orders", new OrderMapper());
    }

    @Override
    public Long createOrder(Order order) {
        String insert = "INSERT INTO orders(auction_id, customer, merchant, price) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setLong(1, order.getAuctionId());
            ps.setString(2, order.getCustomer());
            ps.setString(3, order.getMerchant());
            ps.setDouble(4, order.getPrice());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    private static class OrderMapper implements RowMapper<Order> {
        @Override
        public Order mapRow(ResultSet rs, int i) throws SQLException {
            Order order = new Order();
            order.setId(rs.getLong("id"));
            order.setAuctionId(rs.getLong("auction_id"));
            order.setCustomer(rs.getString("customer"));
            order.setMerchant(rs.getString("merchant"));
            order.setPrice(rs.getDouble("price"));
            return order;
        }
    }
}

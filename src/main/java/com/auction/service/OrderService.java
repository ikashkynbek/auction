package com.auction.service;

import com.auction.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface OrderService {

    List<Order> listOrders();

    Long createOrder(Order order);

}

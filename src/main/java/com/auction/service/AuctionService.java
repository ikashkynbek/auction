/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.auction.service;


import com.auction.model.*;

import java.util.List;

public interface AuctionService {

	List<Auction> listAuctions();
	
	Auction getAuction(String productName);

	Long createAuction(Auction auction);

	List<Order> listOrders();

	Long createOrder(Order order);

	List<Quote> listQuotes(Long auctionId);

	List<Quote> listQuotesGroup(Long auctionId);

	List<Quote> matchingQuotes(Long auctionId, QuoteType type, Double price);

	Long createQuote(Quote quote);

	void updateQuote(Long id, Integer leavesQty, QuoteStatus status);
}

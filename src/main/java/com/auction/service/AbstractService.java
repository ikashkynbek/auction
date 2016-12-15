package com.auction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public abstract class AbstractService {

    protected JdbcTemplate db;

    @Autowired
    public void setScheduleDataSource(@Qualifier("auctionDs") DataSource dataSource) {
        this.db = new JdbcTemplate(dataSource);
    }
}

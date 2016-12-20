package com.auction.model;

import com.auction.utils.gson.GsonHelper;

public class DefaultResponse {

    private Object data;
    private Long timestamp;

    public DefaultResponse(Object data) {
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public String toJson() {
        if (data == null || timestamp == null) {
            data = "Response incomplete, data is null";
            timestamp = System.currentTimeMillis();
        }
        return GsonHelper.toJson(this);
    }
}
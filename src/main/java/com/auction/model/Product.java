package com.auction.model;

import java.util.Map;

/**
 * Created by dansharky on 1/13/17.
 */
public class Product {

    private Long id;
    private String name;
    private Map<String, String> properties;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperty(String property, String propertyVal) {
        this.properties.put(property, propertyVal);
    }

    public String getProperty(String property) {
        return this.properties.get(property);
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}

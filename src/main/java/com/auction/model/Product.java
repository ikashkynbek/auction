package com.auction.model;

import java.util.Map;

/**
 * Created by dansharky on 1/13/17.
 */
public class Product {

    private Long id;
    private String name;
    private Map<ProductProperties, String> properties;

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

    public void setProperty(ProductProperties property, String propertyVal) {
        this.properties.put(property, propertyVal);
    }

    public String getProperty(ProductProperties property) {
        return this.properties.get(property);
    }

    public Map<ProductProperties, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<ProductProperties, String> properties) {
        this.properties = properties;
    }
}

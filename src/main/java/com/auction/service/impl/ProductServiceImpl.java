package com.auction.service.impl;

import com.auction.model.Product;
import com.auction.service.AbstractService;
import com.auction.service.ProductService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dansharky on 1/13/17.
 */
@Service
public class ProductServiceImpl extends AbstractService implements ProductService {

    @Override
    public List<Product> listProducts() {
        List<Product> products = db.query("SELECT * FROM products", new ProductMapper());
        for (Product currProduct : products) {
            Map<String, String> propertyMap =  getProductProperties(currProduct.getId());
            currProduct.setProperties(propertyMap);
        }
        return products;
    }

    @Override
    public Long createProduct(Product product) {
        String insert = "INSERT INTO PRODUCTS(name) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, product.getName());
            return ps;
        }, keyHolder);
        long productID = keyHolder.getKey().longValue();

        String insertProp = "INSERT INTO product_properties (property_name, property_value, product_id) VALUES (?, ?, ?)";
        for (Map.Entry<String, String> entry : product.getProperties().entrySet()) {
            db.update(insertProp, entry.getKey(), entry.getValue(), productID);
        }
        return productID;
    }

    private Map<String, String> getProductProperties(long productId) {
        Map<String, String> result = new HashMap<>();
        List<Map<String,Object>> rows = db.queryForList("SELECT * FROM product_properties WHERE product_id="+productId);
        for (Map m : rows) {
            result.put((String) m.get("property_name"), (String) m.get("property_value"));
        }
        return result;
    }

    private static class ProductMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int i) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            return product;
        }
    }
}

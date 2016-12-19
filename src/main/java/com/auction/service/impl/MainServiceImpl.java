package com.auction.service.impl;

import com.auction.model.Role;
import com.auction.model.User;
import com.auction.service.AbstractService;
import com.auction.service.MainService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class MainServiceImpl extends AbstractService implements MainService {

    @Override
    public Long createUser(String login, String password, String name, Role role) {
        String insert = "INSERT INTO USERS (LOGIN, PASSWORD, NAME, ROLE) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        db.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insert, new String[]{"id"});
            ps.setString(1, login);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, role.name());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public User getUser(String login) {
        List<User> users = db.query("SELECT * FROM USERS WHERE LOGIN=?", new UserMapper(), login);
        return users != null && !users.isEmpty() ? users.get(0) : null;
    }


    private static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("password"));
            user.setName(rs.getString("name"));
            user.setRole(Role.valueOf(rs.getString("role")));
            return user;
        }
    }
}

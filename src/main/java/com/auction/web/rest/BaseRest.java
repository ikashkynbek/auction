package com.auction.web.rest;

import com.auction.model.DefaultResponse;
import com.auction.model.Role;
import com.auction.service.AuctionService;
import com.auction.utils.gson.GsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class BaseRest {

    @Autowired
    protected AuctionService auctionService;

    protected String getUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    protected Role getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        GrantedAuthority role = auth.getAuthorities().iterator().next();
        return Role.valueOf(role.getAuthority());
    }

    protected void returnResponse(HttpServletResponse response, Object obj) {
        PrintWriter out = null;
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(GsonHelper.toJson(obj));
        } catch (Exception ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            if (out != null)
                out.close();
        }
    }

    protected void returnResponse(HttpServletResponse response, String json) {
        PrintWriter out = null;
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.println(json);
        } catch (Exception ex) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            if (out != null)
                out.close();
        }
    }

    protected void renderError(HttpServletResponse response, Exception ex) {
        PrintWriter out = null;
        DefaultResponse jsonResponse;
        String errorJson;
        int code;

        try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();

            if (ex instanceof IllegalStateException ||
                    ex instanceof IllegalArgumentException) {
                jsonResponse = new DefaultResponse(ex.getMessage());
                errorJson = jsonResponse.toJson();
                code = HttpStatus.BAD_REQUEST.value();
            } else if (ex instanceof DuplicateKeyException) {
                jsonResponse = new DefaultResponse("already exists");
                errorJson = jsonResponse.toJson();
                code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            } else {
                jsonResponse = new DefaultResponse("Internal server error");
                errorJson = jsonResponse.toJson();
                code = HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
            response.setStatus(code);
            out.println(errorJson);
        } catch (Exception e) {
            if (out != null)
                out.close();
        }
    }
}

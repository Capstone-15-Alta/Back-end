package com.capstone15.alterra.config;

import com.capstone15.alterra.domain.dao.ThreadDao;
import com.capstone15.alterra.repository.ThreadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

    @Autowired
    private ThreadRepository threadRepository;

    @Value("${app.origin:*}")
    private String origin;

    @Value("${app.production:false}")
    private boolean production;

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        List<ThreadDao> threadDaos = threadRepository.findAll();
        final int total = threadDaos.size();

        if (production) {
            List<String> origins = new ArrayList<>(Arrays.asList(origin.split(",")));
            Optional<String> result = origins
                    .stream()
                    .filter(v -> v.equals(req.getHeader("Origin")))
                    .findFirst();
            result.ifPresent(s -> resp.setHeader("Access-Control-Allow-Origin", s));
        } else {
            resp.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        }

        String methods = "GET, POST, PATCH, PUT, DELETE, OPTIONS";
        String headers = "x-requested-with, Authorization, Content-Type, credential, X-Import-Process, X-XSRF-TOKEN";
        String maxAge = "3600";
        resp.setHeader("Access-Control-Allow-Methods", methods);
        resp.setHeader("Access-Control-Allow-Headers", headers);
        resp.setHeader("Access-Control-Max-Age", maxAge);
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Total-Thread", String.valueOf(total));


        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }

}

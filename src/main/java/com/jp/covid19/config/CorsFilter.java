package com.jp.covid19.config;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        System.out.println("CORSFilter HTTP Request: " + request.getMethod());

        // Authorize (allow) all domains to consume the content

        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Credentials", "true");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Max-Age", "3600");
        ((HttpServletResponse) servletResponse).addHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, Authorization, Cache-control, remember-me");



        HttpServletResponse resp = (HttpServletResponse) servletResponse;

//        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS handshake
//        if (request.getMethod().equals("OPTIONS")) {
//            resp.setStatus(HttpServletResponse.SC_ACCEPTED);
//            return;
//        }

        // pass the request along the filter chain
        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

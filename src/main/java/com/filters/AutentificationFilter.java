package com.filters;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebFilter("/*")
public class AutentificationFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if (httpServletRequest.getServletPath().startsWith("/static/") ||
                httpServletRequest.getServletPath().startsWith("/usercheck")) {

            filterChain.doFilter(request, response);

        } else {
            if (httpServletRequest.getCookies() != null) {
                Map<UUID, Long> authentificationData =
                        (Map<UUID, Long>) request.getServletContext().getAttribute("AUTH_DATA");
                Optional<Cookie> cookie =
                        Arrays.stream(httpServletRequest.getCookies())
                                .filter(c ->
                                        c.getName().equals("SECRET_KEY")
                                ).findFirst();
                if (cookie.isPresent()) {
                    String cookieValue = cookie.get().getValue();
                    try {
                        UUID uuid = UUID.fromString(cookieValue);
                        if (authentificationData.containsKey(uuid)) {
                            filterChain.doFilter(request, response);
                        } else {
                            request.getRequestDispatcher("/login").forward(request, response);
                        }
                    } catch (IllegalArgumentException e) {
                        request.getRequestDispatcher("/login").forward(request, response);
                    }

                } else {
                    request.getRequestDispatcher("/login").forward(request, response);
                }
            }else {
                request.getRequestDispatcher("/login").forward(request, response);
            }
        }
    }
}



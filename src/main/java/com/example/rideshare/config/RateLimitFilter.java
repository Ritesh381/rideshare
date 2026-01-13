package com.example.rideshare.config;

import com.example.rideshare.service.RateLimitService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Use client IP for rate limiting - works for both authenticated and
        // unauthenticated requests
        String clientKey = getClientIP(request);

        // Optionally combine with authenticated user if available
        if (SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !"anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            clientKey = SecurityContextHolder.getContext().getAuthentication().getName();
        }

        boolean allowed = rateLimitService.allowRequest(clientKey);

        if (!allowed) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":429,\"message\":Too many requests}");
            return;
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extract client IP address from request, handling proxies/load balancers
     */
    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            // X-Forwarded-For can contain multiple IPs, the first one is the original
            // client
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIP = request.getHeader("X-Real-IP");
        if (xRealIP != null && !xRealIP.isEmpty()) {
            return xRealIP;
        }

        return request.getRemoteAddr();
    }
}

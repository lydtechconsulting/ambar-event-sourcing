package eventsourcing.common.ambar;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Order(1)
public class AmbarAuthFilter extends OncePerRequestFilter {
    @Value("${app.ambar.username}")
    private String username;

    @Value("${app.ambar.password}")
    private String password;

    private final RequestMappingHandlerMapping handlerMapping;

    public AmbarAuthFilter(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException {

        try {
            HandlerExecutionChain handler = handlerMapping.getHandler(request);
            if (handler == null || !(handler.getHandler() instanceof HandlerMethod)) {
                filterChain.doFilter(request, response);
                return;
            }

            HandlerMethod handlerMethod = (HandlerMethod) handler.getHandler();
            boolean requiresAuth = handlerMethod.getBeanType().isAnnotationPresent(AmbarAuth.class);

            if (!requiresAuth) {
                filterChain.doFilter(request, response);
                return;
            }

            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                throw new ServletException("Properties app.ambar.username and app.ambar.password must be set");
            }

            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Basic authentication required\"}");
                return;
            }

            try {
                String base64Credentials = authHeader.substring("Basic ".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
                String[] parts = credentials.split(":", 2);

                if (parts.length != 2 || !parts[0].equals(username) || !parts[1].equals(password)) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\":\"Invalid credentials\"}");
                    return;
                }

                filterChain.doFilter(request, response);
            } catch (IllegalArgumentException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid authentication format\"}");
            }
        } catch (Exception e) {
            throw new ServletException("Failed to process authentication", e);
        }
    }
}


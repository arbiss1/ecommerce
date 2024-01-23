package ecommerce.web.app.configs;

import ecommerce.web.app.entities.User;
import ecommerce.web.app.service.UserService;
import ecommerce.web.app.shared.ContextHelper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import java.io.IOException;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            final JwtUtils jwtUtils = ContextHelper.getApplicationContext().getBean(JwtUtils.class);

            String username = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                username = jwtUtils.getSubject(jwt);
            }

            if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getByUsername(username);

                if (!JwtUtils.isTokenBlacklisted(jwt) && jwtUtils.validateToken(jwt)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException | AuthenticationException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("User is not logged in!");
        }
    }
}


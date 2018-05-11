package nl.svendubbeld.fontys.auth;

import io.jsonwebtoken.*;
import nl.svendubbeld.fontys.model.User;
import nl.svendubbeld.fontys.model.security.Permission;
import nl.svendubbeld.fontys.model.security.SecurityGroup;
import nl.svendubbeld.fontys.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @see <a href="https://stackoverflow.com/a/26778123">https://stackoverflow.com/a/26778123</a>
 */
@WebFilter("/ws/*")
public class WsAuthenticationFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(WsAuthenticationFilter.class);

    @Inject
    private UserService userService;

    @Inject
    private KeyGenerator keyGenerator;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Transactional
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String token = request.getParameter("authToken");

        if (token == null || token.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }

        User user = null;
        String username = null;

        try {
            Key key = keyGenerator.generateKey();
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);

            if ("access_token".equals(claimsJws.getHeader().get("use"))) {
                username = claimsJws.getBody().getSubject();
                user = userService.findByUsername(username);
            }
        } catch (SignatureException e) {
            logger.warn("Invalid signature detected!", e);
        } catch (ExpiredJwtException e) {
            logger.warn("JWT Token expired", e);
        }

        if (user == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Set<Permission> permissions = user.getSecurityGroups().stream().map(SecurityGroup::getPermissions).flatMap(Collection::stream).collect(Collectors.toSet());
        String finalUsername = username;

        filterChain.doFilter(new HttpServletRequestWrapper(request) {
            @Override
            public Principal getUserPrincipal() {
                return () -> finalUsername;
            }

            @Override
            public boolean isUserInRole(String role) {
                return permissions.contains(Permission.of(role));
            }
        }, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

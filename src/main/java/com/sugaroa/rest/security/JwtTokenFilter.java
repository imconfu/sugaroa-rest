package com.sugaroa.rest.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sugaroa.rest.domain.User;
import com.sugaroa.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component

//TODO RequestHeaderAuthenticationFilter更合适？
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserService userService;

    @Value("Authorization")
    private String token;

    @Value("token")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        String token = request.getHeader(this.token);
        System.out.print("doFilterInternal:" + this.token + ":" + token + "\r\n");

        if (token != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256("secret12");
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("auth0")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    String username = jwt.getClaim("account").asString();
                    User userDetails = this.userService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    //保存用户信息到request中以便其它地方获取使用
                    request.setAttribute("user", userDetails);
                }
            } catch (SignatureVerificationException exception) {
                //Invalid signature/claims
                //result = exception.toString();
                logger.info("JWTVerificationException " + exception.toString());
                throw exception;

            }

        }

        filterChain.doFilter(request, response);
    }
}

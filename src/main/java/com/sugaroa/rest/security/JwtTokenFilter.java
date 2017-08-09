package com.sugaroa.rest.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private UserDetailsService userDetailsService;

    @Value("Authorization")
    private String token;

    @Value("token")
    private String tokenHead;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(this.token);
        System.out.print("doFilterInternal:" + this.token + ":" + token + "\r\n");

        try {
            if(token!=null){
                Algorithm algorithm = Algorithm.HMAC256("secret12");
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("auth0")
                        .build(); //Reusable verifier instance
                DecodedJWT jwt = verifier.verify(token);

                if(SecurityContextHolder.getContext().getAuthentication() == null){
                    String username = jwt.getClaim("account").asString();
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                            request));
                    logger.info("authenticated user " + username + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (UnsupportedEncodingException exception) {
            //UTF-8 encoding not supported
            //result = exception.toString();
            logger.info("UnsupportedEncodingException " + exception.toString());
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            //result = exception.toString();
            logger.info("JWTVerificationException " + exception.toString());

        }

        filterChain.doFilter(request, response);
    }
}

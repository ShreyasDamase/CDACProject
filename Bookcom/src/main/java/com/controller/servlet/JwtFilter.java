package com.controller.servlet;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Claim;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.security.KeyFactory;

public class JwtFilter implements Filter {

    private static final String PUBLIC_KEY = "-----BEGIN PUBLIC KEY-----\n"
            + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs4tgApAK/FTPPHWdQor7\n"
            + "vZ9UuEUbjHLY2YShmLCl0shbo5Nv2rsy6mXDb/ubqYOEGXd9J5TtXtNnWMXcgSfE\n"
            + "4H8aMrZ6ud/MfxsP4VpJTFGWvupfRSmSwhIYddF30DDocOo3XWT9k24czZnfG4XQ\n"
            + "FFTclC6N9DCew6px6NyrMHoxpAS6H+b2lOzNz1h2IMOdXM0c7cas0Hc8cM3fgqjN\n"
            + "rrA1ybEA+5azfY0AuifUWNtnLYYlYlBJusJy36jdb7/0tonbMGCZxycqWIW0bEh3\n"
            + "g610kgjwT205p3KvMAesWeG8qU2mzalwZZitlwtE30zkSwdzWNB4GKIxRgbZIWdz\n"
            + "BQIDAQAB\n"
            + "-----END PUBLIC KEY-----\n";

    private RSAPublicKey getPublicKey(String publicKeyPEM) throws Exception {
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN PUBLIC KEY-----", "")
                                   .replace("-----END PUBLIC KEY-----", "")
                                   .replaceAll("\\s", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic, if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
    	
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        
        try {
            RSAPublicKey publicKey = getPublicKey(PUBLIC_KEY);
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            Map<String,Claim> claims=decodedJWT.getClaims();
            for(Map.Entry<String,Claim> obj :claims.entrySet()) {
            	
            	request.setAttribute(obj.getKey(), obj.getValue().asString());
            }
        } catch (JWTVerificationException e) {
        	System.out.println(e.getMessage());
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
            return;
        } catch (Exception e) {
        
            httpServletResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while verifying the token");
            return;
        }
	        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic, if needed
    }
}

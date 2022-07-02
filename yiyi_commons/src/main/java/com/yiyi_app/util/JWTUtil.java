package com.yiyi_app.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTUtil {

    public static final long TOKEN_EXPIRE_TIME = 7200 * 1000;
    private static final String ISSUER = "yiyi_app";

    public static String createJwtToken(String uid, String secretKey) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return JWT
                .create()
                .withIssuer(ISSUER)
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .withClaim("uid", uid)
                .sign(algorithm);
    }

    public static void verifyToken(String token, String secretKey) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier jwtVerifier = JWT
                    .require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            jwtVerifier.verify(token);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUIDfromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("uid").asString();
    }
}

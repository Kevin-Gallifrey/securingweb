package com.example.securingweb.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

public class JwtUtil {
    // Token过期时间30分钟
    public static final long EXPIRE_TIME = 30 * 60 * 1000;

    // 认证签名私钥
    private static final String secret = "12345667890";

    /* *
     * 验证token是否正确
     * 若验证失败，会抛出JWTVerificationException
     */
    public static void verify(String token, String username) throws Exception{
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("securingweb@example.com")
                .withClaim("username", username)
                .build(); //Reusable verifier instance
            verifier.verify(token);
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            throw exception;
        } catch (UnsupportedEncodingException exception){
            throw exception;
        }

    }
    
    /* *
     * 生成token
     * 在token中附带username信息
     */
    public static String sign(String username) {
        try {
            // 设置过期时间
            Date exp = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 设置签名算法
            Algorithm algorithm = Algorithm.HMAC256(secret);

            // 生成token，附带username信息
            String token = JWT.create()
                .withClaim("username", username)
                .withIssuer("securingweb@example.com")
                .withExpiresAt(exp)
                .sign(algorithm);

            return token;
        } catch (UnsupportedEncodingException exception) {
            return null;
        }
        
    }

    /* *
     * 从token中获得用户名
     */
    public static String getUserNameByToken(String token)  {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username")
                .asString();
    }

}

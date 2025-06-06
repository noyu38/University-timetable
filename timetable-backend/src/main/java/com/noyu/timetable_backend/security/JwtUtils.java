package com.noyu.timetable_backend.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {
        // 認証されたユーザーのプリンシパル（本体）を取得
        org.springframework.security.core.userdetails.User userPrincipal = (org.springframework.security.core.userdetails.User) authentication
                .getPrincipal();

        Date now = new Date();
        Date expityDate = new Date(now.getTime() + jwtExpirationMs);

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

        return Jwts.builder()
                .subject(userPrincipal.getUsername()) // トークンの主題としてユーザー名を設定
                .issuedAt(now) // 発行日時
                .expiration(expityDate) // 有効期限
                .signWith(key) // 秘密鍵で署名
                .compact(); // コンパクトな文字列形式に変換
    }
}

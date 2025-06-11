package com.noyu.timetable_backend.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.noyu.timetable_backend.security.JwtUtils;
import com.noyu.timetable_backend.service.UserDetailsServiceImpl;

import org.springframework.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
    // OncePerRequestFilter: １つのリクエストに対して１度だけ実行されることを保証するフィルター

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("AuthTokenFilter: リクエスト URI -> {}", request.getRequestURI());
        try {
            // リクエストヘッダーからJWTを取得
            String jwt = parseJwt(request);

            logger.info("AuthTokenFilter: パースされたJWT -> {}", jwt);

            // JWTが存在するか
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                logger.info("AuthTokenFilter: JWTの検証に成功しました。");
                // JWTからユーザー名を取得
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // ユーザー名からUserDetailsを取得
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 認証トークンを作成
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // パスワードは不要
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // SecurityContextHolderに認証情報を設定
                SecurityContextHolder.getContext().setAuthentication(authentication);

                logger.info("AuthTokenFilter: SecurityContextHolderに認証情報を設定しました。");
            } else {
                logger.warn("AuthTokenFilter: JWTが見つからない、または無効です。");
            }
        } catch (Exception e) {
            logger.error("ユーザー認証を設定できません: {}", e.getMessage());
        }

        // 次のフィルターに処理を渡す
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        logger.info("AuthTokenFilter: Authorizationヘッダー -> {}", headerAuth);

        // ヘッダーに"Bearer"という文字列が含まれていれば、その後ろの部分を返す"
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
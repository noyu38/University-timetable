package com.noyu.timetable_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noyu.timetable_backend.dto.SignUpRequestDTO;
import com.noyu.timetable_backend.service.UserService;
import com.noyu.timetable_backend.security.JwtUtils;
import com.noyu.timetable_backend.dto.JwtResponseDTO;
import com.noyu.timetable_backend.dto.LoginRequestDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthController(UserService userService,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    // @RequestBody HTTPリクエストのボディのJSONをSignUpRequestDTOオブジェクトに変換して受け取る
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequestDTO signUpRequest) {

        try {
            logger.info("ユーザー登録リクエストを受け付けました: {}", signUpRequest.getUsername());
            userService.registerNewUser(signUpRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body("ユーザー登録に成功しました！");
        } catch (RuntimeException e) {
            logger.error("ユーザー登録に失敗しました: {}", e.getMessage());

            // HTTPステータス 400 Bad Requestを返す
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        try {
            // 認証の実行
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            // SecurityContextに認証情報を設定
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // JWTを作成
            String jwt = jwtUtils.generateJwtToken(authentication);

            return ResponseEntity.ok(new JwtResponseDTO(jwt));
        } catch (AuthenticationException e) {
            logger.error("認証に失敗しました: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ユーザー名またはパスワードが無効です。");
        }
    }
}
package com.noyu.timetable_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noyu.timetable_backend.dto.SignUpRequestDTO;
import com.noyu.timetable_backend.model.User;
import com.noyu.timetable_backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
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
}

package com.noyu.timetable_backend.service;

// import org.apache.catalina.User;
import com.noyu.timetable_backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noyu.timetable_backend.dto.SignUpRequestDTO;
import com.noyu.timetable_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(SignUpRequestDTO signUpRequest) {
        // ユーザー名が既に存在するかチェック
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new UserAlreadyExistsException("エラー: ユーザー名 " + signUpRequest.getUsername() + " は既に存在します。");
        }

        // メールアドレスが既に存在するかチェック
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new UserAlreadyExistsException("エラー: メールアドレス " + signUpRequest.getEmail() + " は既に存在します。");
        }

        // Userオブジェクトを作成
        User newUser = new User();
        newUser.setUsername(signUpRequest.getUsername());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        // ユーザーをデータベースに保存
        return userRepository.save(newUser);
    }
}

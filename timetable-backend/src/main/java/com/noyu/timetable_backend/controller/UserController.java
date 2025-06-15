package com.noyu.timetable_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noyu.timetable_backend.dto.UserEducationUpdateRequestDTO;
import com.noyu.timetable_backend.service.UserService;
import com.noyu.timetable_backend.dto.UserDTO;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUserProfile(@AuthenticationPrincipal UserDetails currentUser) {

        // ユーザー情報を取得してDTOに変換(ユーザー情報は@AuthenticationPrincipalで自動的に取得される)
        UserDTO userProfile = userService.getUserProfile(currentUser.getUsername());

        return ResponseEntity.ok(userProfile);
    }

    @PutMapping("/{username}/education")
    public ResponseEntity<?> updateUserEducation(
            // @PathVariable URLの{username}部分を受け取る
            // @RequestBody HTTPリクエストのボディのJSONをUserEducationUpdateRequestDTOオブジェクトに変換して受け取る
            @PathVariable String username,
            @RequestBody UserEducationUpdateRequestDTO requestDTO) {

        try {
            logger.info("{}の学部・学科情報を更新します: 学部ID {}, 学科ID {}",
                    username,
                    requestDTO.getFacultyId(),
                    requestDTO.getDepartmentId());

            UserDTO updatedUserDTO = userService.updateUserEducation(username, requestDTO);

            return ResponseEntity.ok(updatedUserDTO);
        } catch (EntityNotFoundException e) {
            logger.error("エンティティが見つかりません: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("不正な引数です: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            logger.error("学部・学科情報の更新中にエラーが発生しました for user {}: {}", username, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    // @PutMapping("/me/education")
    // public ResponseEntity<?> updateCurrentUserEducation(
    // @AuthenticationPrincipal UserDetails currentUser,
    // @RequestBody UserEducationUpdateRequestDTO requestDTO) {

    // if (currentUser == null) {
    // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("認証されていません。");
    // }
    // String username = currentUser.getUsername();
    // return ResponseEntity.ok("学部・学科情報が更新されました。");
    // }
}

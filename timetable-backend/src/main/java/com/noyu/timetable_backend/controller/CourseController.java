package com.noyu.timetable_backend.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.noyu.timetable_backend.dto.CourseDTO;
import com.noyu.timetable_backend.dto.CreateCourseRequestDTO;
import com.noyu.timetable_backend.model.User;
import com.noyu.timetable_backend.service.CourseService;
import com.noyu.timetable_backend.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAvailableCourses(@AuthenticationPrincipal UserDetails currentUser) {
        // @AuthenticationPrincipal ログインしているユーザーの情報を取得
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            // ユーザー名からUserエンティティを取得
            User user = userService.findUserByUsername(currentUser.getUsername());

            if (user.getDepartment() == null) {
                // ? いったんBad Requestを返してるけど、もしかしたら共通科目だけを返した方がいいかも？
                return ResponseEntity.badRequest().body("学部・学科が設定されていません。");
            }

            Long departmentId = user.getDepartment().getId();
            List<CourseDTO> courses = courseService.getAvailableCourses(departmentId);
            return ResponseEntity.ok(courses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body("ユーザー情報が見つかりません。");
        }
    }

    @PostMapping
    public ResponseEntity<?> createCustomCourse(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestBody CreateCourseRequestDTO requestDTO) {

        try {
            String username = currentUser.getUsername();
            CourseDTO newCourse = courseService.createCustomCourse(username, requestDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
        } catch (EntityNotFoundException e) {
            logger.warn("授業作成失敗（ユーザーが見つかりません）: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException | IllegalArgumentException e) {
            logger.warn("授業作成失敗（不正なリクエスト）: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            logger.error("予期せぬエラーが発生しました。", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

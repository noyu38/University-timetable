package com.noyu.timetable_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noyu.timetable_backend.dto.CourseDTO;
import com.noyu.timetable_backend.model.User;
import com.noyu.timetable_backend.service.CourseService;
import com.noyu.timetable_backend.service.UserService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAvailableCourses(@AuthenticationPrincipal UserDetails currentUser) {
        // @AuthenticationPrincipal ログインしているユーザーの情報を取得
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        try {
            // ユーザー名からUserエンティティを取得
            User user = userService.findUserByUsername(currentUser.getUsername());

            if (user.getDepartment() == null) {
                // ? いったんBad Requestを返してるけど、もしかしたら共通科目だけを返した方がいいかも？
                return ResponseEntity.badRequest().build();
            }

            Long departmentId = user.getDepartment().getId();
            List<CourseDTO> courses = courseService.getAvailableCourses(departmentId);
            return ResponseEntity.ok(courses);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
}

package com.noyu.timetable_backend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noyu.timetable_backend.dto.CourseDTO;
import com.noyu.timetable_backend.model.Course;
import com.noyu.timetable_backend.repository.CourseRepository;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getAvailableCourses(Long departmentId) {
        // ユーザーの所属学科向けの授業を取得
        List<Course> departmentCourses = courseRepository.findByDepartmentId(departmentId);

        // 全額共通科目などを取得
        List<Course> commonCourses = courseRepository.findByDepartmentIsNull();

        // 2つのリストを結合し、DTOに変換して返す
        return Stream.concat(departmentCourses.stream(), commonCourses.stream())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // CourseエンティティをCourseDTOに変換
    private CourseDTO convertToDTO(Course course) {
        Long departmentId = (course.getDepartment() != null) ? course.getDepartment().getId() : null;
        String departmentName = (course.getDepartment().getName() != null) ? course.getDepartment().getName() : "共通";
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getRoom(),
                course.getTeacher(),
                departmentId,
                departmentName);
    }
}

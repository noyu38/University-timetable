package com.noyu.timetable_backend.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noyu.timetable_backend.dto.CourseDTO;
import com.noyu.timetable_backend.dto.CreateCourseRequestDTO;
import com.noyu.timetable_backend.model.Course;
import com.noyu.timetable_backend.model.Department;
import com.noyu.timetable_backend.model.User;
import com.noyu.timetable_backend.repository.CourseRepository;
import com.noyu.timetable_backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
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
        String departmentName = (course.getDepartment() != null) ? course.getDepartment().getName() : "共通";
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getRoom(),
                course.getTeacher(),
                departmentId,
                departmentName);
    }

    @Transactional
    public CourseDTO createCustomCourse(String username, CreateCourseRequestDTO requestDTO) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("ユーザーが見つかりません: " + username));

        Department userDepartment = user.getDepartment();
        if (userDepartment == null) {
            throw new IllegalArgumentException("専門科目を追加するには、まず学科を登録してください。");
        }

        // 同じ学科に同じ名前の授業がすでに存在しないか確認
        if (courseRepository.existsByNameAndDepartment(requestDTO.getName(), userDepartment)) {
            throw new IllegalArgumentException(userDepartment.getName() + "にはすでに同じ名前の授業が存在します。");
        }

        Course newCourse = new Course();
        newCourse.setName(requestDTO.getName());
        if (requestDTO.getRoom() == null) {
            newCourse.setRoom("");
        } else {
            newCourse.setRoom(requestDTO.getRoom());
        }
        if (requestDTO.getTeacher() == null) {
            newCourse.setTeacher("");
        } else {
            newCourse.setTeacher(requestDTO.getTeacher());
        }
        newCourse.setDepartment(userDepartment);

        Course savedCourse = courseRepository.save(newCourse);

        return convertToDTO(savedCourse);
    }
}

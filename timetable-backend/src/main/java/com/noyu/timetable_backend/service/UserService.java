package com.noyu.timetable_backend.service;

// import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.noyu.timetable_backend.dto.UserDTO;
import com.noyu.timetable_backend.dto.DepartmentDTO;
import com.noyu.timetable_backend.dto.FacultyDTO;
import com.noyu.timetable_backend.dto.SignUpRequestDTO;
import com.noyu.timetable_backend.dto.UserEducationUpdateRequestDTO;
import com.noyu.timetable_backend.model.Department;
import com.noyu.timetable_backend.model.Faculty;
import com.noyu.timetable_backend.model.User;
import com.noyu.timetable_backend.repository.DepartmentRepository;
import com.noyu.timetable_backend.repository.FacultyRepository;
import com.noyu.timetable_backend.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        FacultyDTO facultyDTO = null;
        if (user.getFaculty() != null) {
            facultyDTO = new FacultyDTO(user.getFaculty().getId(), user.getFaculty().getName());
        }

        DepartmentDTO departmentDTO = null;
        if (user.getDepartment() != null) {
            departmentDTO = new DepartmentDTO(user.getDepartment().getId(), user.getDepartment().getName());
        }

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                facultyDTO,
                departmentDTO);
    }

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            FacultyRepository facultyRepository,
            DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
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

    @Transactional
    public UserDTO updateUserEducation(String username, UserEducationUpdateRequestDTO requestDTO) {
        // ユーザー名でエンティティを取得
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("ユーザーが見つかりません。: " + username));

        // DTOから学部IDを取得し、学部エンティティを取得
        Faculty faculty = facultyRepository.findById(requestDTO.getFacultyId())
                .orElseThrow(() -> new EntityNotFoundException("学部が見つかりません: " + requestDTO.getFacultyId()));

        // DTOから学科IDを取得し、学科エンティティを取得
        Department department = departmentRepository.findById(requestDTO.getDepartmentId())
                .orElseThrow(() -> new EntityNotFoundException("学科が見つかりません: " + requestDTO.getDepartmentId()));

        if (!department.getFaculty().getId().equals(faculty.getId())) {
            throw new IllegalArgumentException(
                    "選択された学科「" + department.getName() + "」は学部「" + faculty.getName() + "」に所属していません。");
        }

        user.setFaculty(faculty);
        user.setDepartment(department);
        User updateUser = userRepository.save(user);

        return convertToUserDTO(updateUser);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("ユーザーが見つかりません: " + username));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public UserDTO getUserProfile(String username) {
        User user = findUserByUsername(username);
        return convertToUserDTO(user);
    }
}

package com.noyu.timetable_backend.service;

import com.noyu.timetable_backend.model.Department;
import com.noyu.timetable_backend.repository.DepartmentRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.noyu.timetable_backend.dto.DepartmentDTO;

public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<DepartmentDTO> getDepartmentsByFacultyId(Long facultyId) {
        List<Department> departments = departmentRepository.findByFacultyId(facultyId);

        return departments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO convertToDTO(Department department) {
        return new DepartmentDTO(department.getId(), department.getName());
    }
}

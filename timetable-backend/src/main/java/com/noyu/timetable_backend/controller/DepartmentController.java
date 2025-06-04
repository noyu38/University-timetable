package com.noyu.timetable_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.noyu.timetable_backend.dto.DepartmentDTO;
import com.noyu.timetable_backend.service.DepartmentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getDepartmentsByFacultyId(@RequestParam Long facultyId) {
        List<DepartmentDTO> departments = departmentService.getDepartmentsByFacultyId(facultyId);

        if (departments.isEmpty()) {
            return ResponseEntity.ok(departments);
            // ? not foundを返すか検討
            // return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(departments);
    }

}

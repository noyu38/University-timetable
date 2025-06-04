package com.noyu.timetable_backend.repository;

import com.noyu.timetable_backend.model.Department;
import com.noyu.timetable_backend.model.Faculty;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    Optional<Department> findByName(String name);

    List<Department> findByFaculty(Faculty faculty);

    List<Department> findByFacultyId(Long facultyId);

    Optional<Department> findByNameAndFacultyId(String name, Long facultyId);

    List<Department> findByNameContainingAndFacultyId(String nameKeyword, Long facultyId);
}

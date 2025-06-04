package com.noyu.timetable_backend.repository;

import com.noyu.timetable_backend.model.Faculty;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    
    Optional<Faculty> findById(Long id);

    Optional<Faculty> findByName(String name);
}

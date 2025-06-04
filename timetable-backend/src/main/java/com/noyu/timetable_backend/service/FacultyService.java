package com.noyu.timetable_backend.service;

import com.noyu.timetable_backend.dto.FacultyDTO;
import com.noyu.timetable_backend.model.Faculty;
import com.noyu.timetable_backend.repository.FacultyRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FacultyService {
    
    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    // すべての学部情報を取得し、FacultyDTOのリストに変換する
    public List<FacultyDTO> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll(); // リポジトリからすべてのFacultyエンティティを取得
        // FacultyエンティティのリストをFacultyDTOのリストに変換
        return faculties.stream()
                       .map((this::convertToDTO))
                       .collect(Collectors.toList());
    }

    private FacultyDTO convertToDTO(Faculty faculty) {
        return new FacultyDTO(faculty.getId(), faculty.getName());
    }
}

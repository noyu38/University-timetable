package com.noyu.timetable_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.noyu.timetable_backend.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * 指定された学科IDに所属する授業をすべて検索
     * 
     * @param departmentId 検索対象の学科ID
     * @return 見つかった授業のリスト
     */
    List<Course> findByDepartmentId(Long departmentId);

    /**
     * 特定の学科に所属していない授業（全額共通科目など）をすべて検索
     * 
     * @return 見つかった授業のリスト
     */
    List<Course> findByDepartmentIsNull();

    /**
     * 授業名に特定の文字列を含む授業を検索（部分一致検索）
     * 
     * @param nameKeyword 検索キーワード
     * @return 見つかった授業のリスト
     */
    List<Course> findByNameContaining(String nameKeyword);
}

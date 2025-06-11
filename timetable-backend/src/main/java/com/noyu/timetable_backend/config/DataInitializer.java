package com.noyu.timetable_backend.config;

import com.noyu.timetable_backend.model.Course; // Courseをインポート
import com.noyu.timetable_backend.model.Department;
import com.noyu.timetable_backend.model.Faculty;
import com.noyu.timetable_backend.repository.CourseRepository; // CourseRepositoryをインポート
import com.noyu.timetable_backend.repository.DepartmentRepository;
import com.noyu.timetable_backend.repository.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository; // CourseRepositoryを追加

    // コンストラクタを修正
    public DataInitializer(FacultyRepository facultyRepository,
            DepartmentRepository departmentRepository,
            CourseRepository courseRepository) { // CourseRepositoryを追加
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository; // CourseRepositoryを追加
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.info("データベースの初期化を開始します...");

        // --- 学部・学科データの初期化 ---
        // (この部分は既存のコードのまま)
        if (facultyRepository.count() == 0) {
            logger.info("学部・学科データを登録します。");
            Faculty faculty1 = new Faculty("文学部");
            Faculty faculty2 = new Faculty("工学部");
            Faculty faculty3 = new Faculty("経済学部");

            facultyRepository.saveAll(Arrays.asList(faculty1, faculty2, faculty3));

            Department dept1_1 = new Department("日本文学科", faculty1);
            Department dept1_2 = new Department("英米文学科", faculty1);
            departmentRepository.saveAll(Arrays.asList(dept1_1, dept1_2));

            Department dept2_1 = new Department("機械工学科", faculty2);
            Department dept2_2 = new Department("電気電子工学科", faculty2);
            Department dept2_3 = new Department("情報工学科", faculty2);
            departmentRepository.saveAll(Arrays.asList(dept2_1, dept2_2, dept2_3));

            Department dept3_1 = new Department("経済学科", faculty3);
            Department dept3_2 = new Department("経営学科", faculty3);
            departmentRepository.saveAll(Arrays.asList(dept3_1, dept3_2));
            logger.info("学部・学科データを登録しました。");
        } else {
            logger.info("学部・学科データは既に存在するため、スキップします。");
        }

        // --- ここから授業データの初期化を追加 ---
        if (courseRepository.count() == 0) {
            logger.info("授業データを登録します。");
            // 登録済みの学科を取得して、授業に紐付ける
            Department deptInfo = departmentRepository.findByName("情報工学科").orElse(null);
            Department deptKeiei = departmentRepository.findByName("経営学科").orElse(null);

            // --- 専門科目 ---
            if (deptInfo != null) {
                Course courseInfo1 = new Course("プログラミング基礎 I", "7-101", "山田太郎", deptInfo);
                Course courseInfo2 = new Course("データ構造とアルゴリズム", "7-203", "鈴木一郎", deptInfo);
                courseRepository.saveAll(Arrays.asList(courseInfo1, courseInfo2));
            }
            if (deptKeiei != null) {
                Course courseKeiei1 = new Course("マーケティング論", "3-305", "佐藤花子", deptKeiei);
                courseRepository.save(courseKeiei1);

            }

            // --- 全学共通科目 (departmentをnullにする) ---
            Course commonCourse1 = new Course("情報リテラシー", "PC-1", "田中次郎", null);
            Course commonCourse2 = new Course("統計学入門", "5-101", "高橋三郎", null);
            courseRepository.saveAll(Arrays.asList(commonCourse1, commonCourse2));

            logger.info("授業データを登録しました。");
        } else {
            logger.info("授業データは既に存在するため、スキップします。");
        }
        // --- ここまで授業データの初期化 ---

        logger.info("データベースの初期化が完了しました。");
    }
}
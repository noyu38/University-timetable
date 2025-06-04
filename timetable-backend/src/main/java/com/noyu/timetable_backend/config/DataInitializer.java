package com.noyu.timetable_backend.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.noyu.timetable_backend.model.Department;
import com.noyu.timetable_backend.model.Faculty;
import com.noyu.timetable_backend.repository.DepartmentRepository;
import com.noyu.timetable_backend.repository.FacultyRepository;

@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;

    // コンストラクタインジェクションでリポジトリを注入
    public DataInitializer(FacultyRepository facultyRepository, DepartmentRepository departmentRepository) {
        this.facultyRepository = facultyRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    @Transactional // このメソッド内のデータベース操作を一つのトランザクションとして実行する
    public void run(String... args) throws Exception {
        logger.info("データベースの初期化を開始します...");

        // --- 学部データの初期化 ---
        // 既に学部データが存在する場合は、何もしない (単純な例)
        if (facultyRepository.count() == 0) {
            logger.info("学部データを登録します。");
            Faculty faculty1 = new Faculty("文学部");
            Faculty faculty2 = new Faculty("工学部");
            Faculty faculty3 = new Faculty("経済学部");

            // saveAllを使うと複数のエンティティを一度に保存できる
            List<Faculty> savedFaculties = facultyRepository.saveAll(Arrays.asList(faculty1, faculty2, faculty3));
            logger.info("登録された学部: {}", savedFaculties);

            // --- 学科データの初期化 (上記で保存した学部に関連付ける) ---
            // 既に学科データが存在する場合は、何もしない (単純な例)
            if (departmentRepository.count() == 0) {
                logger.info("学科データを登録します。");
                // 保存されたFacultyオブジェクトを使ってDepartmentを作成
                Faculty savedFaculty1 = savedFaculties.stream().filter(f -> "文学部".equals(f.getName())).findFirst()
                        .orElse(null);
                Faculty savedFaculty2 = savedFaculties.stream().filter(f -> "工学部".equals(f.getName())).findFirst()
                        .orElse(null);
                Faculty savedFaculty3 = savedFaculties.stream().filter(f -> "経済学部".equals(f.getName())).findFirst()
                        .orElse(null);

                if (savedFaculty1 != null) {
                    Department dept1_1 = new Department("日本文学科", savedFaculty1);
                    Department dept1_2 = new Department("英米文学科", savedFaculty1);
                    departmentRepository.saveAll(Arrays.asList(dept1_1, dept1_2));
                }

                if (savedFaculty2 != null) {
                    Department dept2_1 = new Department("機械工学科", savedFaculty2);
                    Department dept2_2 = new Department("電気電子工学科", savedFaculty2);
                    Department dept2_3 = new Department("情報工学科", savedFaculty2);
                    departmentRepository.saveAll(Arrays.asList(dept2_1, dept2_2, dept2_3));
                }

                if (savedFaculty3 != null) {
                    Department dept3_1 = new Department("経済学科", savedFaculty3);
                    Department dept3_2 = new Department("経営学科", savedFaculty3);
                    departmentRepository.saveAll(Arrays.asList(dept3_1, dept3_2));
                }
                logger.info("学科データを登録しました。");
            } else {
                logger.info("学科データは既に存在するため、スキップします。");
            }

        } else {
            logger.info("学部データは既に存在するため、スキップします。");
        }
        logger.info("データベースの初期化が完了しました。");
    }
}

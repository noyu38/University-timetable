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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository; // CourseRepositoryを追加

    private static final int COURSE_NAME = 0;
    private static final int COURSE_ROOM = 1;
    private static final int COURSE_TEACHER = 2;

    private static final List<List<String>> CS_COURSES = new ArrayList<>(Arrays.asList());

    private static final List<List<String>> BI_COURSES = new ArrayList<>(Arrays.asList(

    ));

    private static final List<List<String>> IA_COURSES = new ArrayList<>(Arrays.asList(

    ));

    private static final List<List<String>> KIKAI_COURSES = new ArrayList<>(Arrays.asList(

    ));

    private static final List<List<String>> DENDEN_COURSES = new ArrayList<>(Arrays.asList(

    ));

    private static final List<List<String>> DENBUTU_COURSES = new ArrayList<>(Arrays.asList(

    ));

    private static final List<List<String>> KABAI_COURSES = new ArrayList<>(Arrays.asList(

    ));

    private static final List<List<String>> SUSISU_COURSES = new ArrayList<>(Arrays.asList(

    ));

    private static final List<List<String>> ZENGAKU_COURSES = new ArrayList<>(Arrays.asList(
            Arrays.asList("中級英語", "S-Port2Fセミナールーム", "柳澤 ナタリー")));

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
        if (facultyRepository.count() == 0) {
            logger.info("学部・学科データを登録します。");
            Faculty faculty1 = new Faculty("情報学部");
            Faculty faculty2 = new Faculty("工学部");

            facultyRepository.saveAll(Arrays.asList(faculty1, faculty2));

            Department dept1_1 = new Department("情報科学科", faculty1);
            Department dept1_2 = new Department("行動情報学科", faculty1);
            Department dept1_3 = new Department("情報社会学科", faculty1);
            departmentRepository.saveAll(Arrays.asList(dept1_1, dept1_2, dept1_3));

            Department dept2_1 = new Department("機械工学科", faculty2);
            Department dept2_2 = new Department("電気電子工学科", faculty2);
            Department dept2_3 = new Department("電子物質科学科", faculty2);
            Department dept2_4 = new Department("化学バイオ工学科", faculty2);
            Department dept2_5 = new Department("数理システム工学科", faculty2);
            departmentRepository.saveAll(Arrays.asList(dept2_1, dept2_2, dept2_3, dept2_4, dept2_5));

            logger.info("学部・学科データを登録しました。");
        } else {
            logger.info("学部・学科データは既に存在するため、スキップします。");
        }

        // --- ここから授業データの初期化を追加 ---
        if (courseRepository.count() == 0) {
            logger.info("静岡大学の授業データを登録します。");
            // 登録済みの学科を取得して、授業に紐付ける
            Department deptCS = departmentRepository.findByName("情報科学科").orElse(null);
            Department deptBI = departmentRepository.findByName("行動情報学科").orElse(null);
            Department deptIA = departmentRepository.findByName("情報社会学科").orElse(null);
            // 機械工学科
            Department deptKIKAI = departmentRepository.findByName("機械工学科").orElse(null);
            // 電子電気工学科
            Department deptDENDEN = departmentRepository.findByName("電子電気工学科").orElse(null);
            // 電子物質科学科
            Department deptDENBUTU = departmentRepository.findByName("電子物質科学科").orElse(null);
            // 化学バイオ工学科
            Department deptKABAI = departmentRepository.findByName("化学バイオ工学科").orElse(null);
            // 数理システム工学科
            Department deptSUSISU = departmentRepository.findByName("数理システム工学科").orElse(null);

            // 専門科目を登録
            if (deptCS != null) {
                registerCourses(CS_COURSES, deptCS);
            }
            if (deptBI != null) {
                registerCourses(BI_COURSES, deptBI);
            }
            if (deptIA != null) {
                registerCourses(IA_COURSES, deptBI);
            }
            if (deptKIKAI != null) {
                registerCourses(KIKAI_COURSES, deptKIKAI);
            }
            if (deptDENDEN != null) {
                registerCourses(DENDEN_COURSES, deptDENDEN);
            }
            if (deptDENBUTU != null) {
                registerCourses(DENBUTU_COURSES, deptDENBUTU);
            }
            if (deptKABAI != null) {
                registerCourses(KABAI_COURSES, deptKABAI);
            }
            if (deptSUSISU != null) {
                registerCourses(SUSISU_COURSES, deptSUSISU);
            }

            // --- 全学共通科目 (departmentをnullにする) ---
            registerCourses(ZENGAKU_COURSES, null);

            logger.info("授業データを登録しました。");
        } else {
            logger.info("授業データは既に存在するため、スキップします。");
        }
        // --- ここまで授業データの初期化 ---

        logger.info("データベースの初期化が完了しました。");
    }

    // 時間割に授業を登録する処理
    private void registerCourses(List<List<String>> details, Department dept) {
        for (List<String> detail : details) {
            Course course = new Course(detail.get(COURSE_NAME), detail.get(COURSE_ROOM), detail.get(COURSE_TEACHER),
                    dept);
            courseRepository.save(course);
        }
    }
}
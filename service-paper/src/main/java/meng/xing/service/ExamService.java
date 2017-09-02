package meng.xing.service;

import meng.xing.entity.Exam;
import meng.xing.entity.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExamService {

    Page<Exam> findSomeExamsBySubject(Subject subject, Pageable pageable);

    Exam findExamById(Long id);

    boolean addExam(Exam exam);

    boolean updateExam(Exam exam);

    boolean deleteExamById(Long exam);


}

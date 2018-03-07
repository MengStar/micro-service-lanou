package meng.xing.repository;

import meng.xing.entity.UserDoneExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDoneExamRepository extends JpaRepository<UserDoneExam, Long> {
    UserDoneExam findByUsernameAndExamId(String username, Long examId);
}

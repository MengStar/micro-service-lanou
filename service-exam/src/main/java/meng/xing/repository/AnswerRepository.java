package meng.xing.repository;

import meng.xing.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 */
@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findByUsernameAndPaperId(String username, long paperId);
}

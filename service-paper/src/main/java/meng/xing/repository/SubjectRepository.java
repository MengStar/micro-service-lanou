package meng.xing.repository;

import meng.xing.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/7/23.
 */
@Repository
public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Subject findByType(String type);
}

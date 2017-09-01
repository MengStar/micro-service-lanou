package meng.xing.repository;

import meng.xing.entity.Subject;
import meng.xing.entity.TestItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2017/7/23.
 */
@Repository

public interface TestItemRepository extends JpaRepository<TestItem, Long> {
    Page<TestItem> findByTypeAndSubject(String type, Subject subject, Pageable pageable);

    Page<TestItem> findByType(String type, Pageable pageable);
}

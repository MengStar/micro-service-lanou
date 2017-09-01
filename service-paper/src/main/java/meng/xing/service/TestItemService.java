package meng.xing.service;

import meng.xing.entity.Subject;
import meng.xing.entity.TestItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TestItemService {

    Page<TestItem> findTestItemsByTypeAndSubject(String type, Subject subject, Pageable pageable);

    boolean addTestItme(TestItem testItem);

    boolean updateTestItem(TestItem testItem);

    boolean deleteTestItemById(Long id);

    TestItem findTestItemById(Long id);
}

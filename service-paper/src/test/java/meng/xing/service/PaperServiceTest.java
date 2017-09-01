package meng.xing.service;

import meng.xing.entity.Paper;
import meng.xing.entity.Subject;
import meng.xing.entity.TestItem;
import meng.xing.repository.SubjectRepository;
import meng.xing.repository.TestItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.awt.print.Pageable;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaperServiceTest {
    @Autowired
    PaperService paperService;
    @Autowired
    TestItemRepository testItemRepository;
    @Autowired
    SubjectRepository subjectRepository;
    private static PageRequest pageable;

    static {
        Sort _sort = new Sort(Sort.Direction.fromString("asc"), "id");
        //传来的页码是从1开始，而服务器从1开始算
        pageable = new PageRequest(0, 1, _sort);
    }

    @Test
    public void findAllPapersBySubject() throws Exception {
        Assert.assertNotNull(paperService.findAllPapersBySubject(subjectRepository.findByType("JAVA"), pageable));
    }

    @Test
    public void findPaperById() throws Exception {
        Assert.assertNotNull(paperService.findPaperById((long) 1));
    }

    @Test
    public void addPaper() throws Exception {
        Set<TestItem> testItems = new HashSet<>();
        testItems.addAll(testItemRepository.findAll());
        Subject subject = subjectRepository.findByType("JAVA");
        Assert.assertEquals(paperService.addPaper(new Paper("admin", "测试用例", subject, testItems)), true);
    }

    @Test
    public void updatePaper() throws Exception {
        Set<TestItem> testItems = new HashSet<>();
        testItems.addAll(testItemRepository.findAll());
        Subject subject = subjectRepository.findByType("WEB");
        Assert.assertEquals(paperService.updatePaper(new Paper("admin", "测试试卷", subject, testItems)), true);
    }

    @Test
    public void deletePaperById() throws Exception {
        Assert.assertEquals(paperService.deletePaperById((long) 1), true);

    }

}
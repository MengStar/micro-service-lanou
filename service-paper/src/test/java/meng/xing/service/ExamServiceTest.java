package meng.xing.service;

import meng.xing.entity.Exam;
import meng.xing.entity.Paper;
import meng.xing.entity.Subject;
import meng.xing.repository.PaperRepository;
import meng.xing.repository.SubjectRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;



@RunWith(SpringRunner.class)
@SpringBootTest
public class ExamServiceTest {
    @Autowired
    ExamService examService;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    PaperRepository paperRepository;
    private static PageRequest pageable;

    static {
        Sort _sort = new Sort(Sort.Direction.fromString("asc"), "id");
        //传来的页码是从1开始，而服务器从1开始算
        pageable = new PageRequest(0, 1, _sort);
    }

    @Test
    public void findSomeExamsBySubject() throws Exception {
        Assert.assertNotNull(examService.findSomeExamsBySubject(subjectRepository.findByType("JAVA"), pageable));
    }

    @Test
    public void findExamById() throws Exception {
        Assert.assertNotNull(examService.findExamById((long) 1));
    }

    @Test
    public void addExam() throws Exception {
        Subject java = subjectRepository.findByType("JAVA");
        Paper paper = paperRepository.findOne((long) 1);
        Assert.assertEquals(examService.addExam(new Exam("这是一场测试考试" , java, paper, "admin")), true);
    }

    @Test
    public void updateExam() throws Exception {
        Exam exam = examService.findExamById((long) 1);
        exam.setDescription("测试用例");
        Assert.assertEquals(examService.updateExam(exam), true);
    }

    @Test
    public void deleteExamById() throws Exception {
        Assert.assertEquals(examService.deleteExamById((long) 2), true);
    }

}
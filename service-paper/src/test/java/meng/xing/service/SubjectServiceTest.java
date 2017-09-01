package meng.xing.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SubjectServiceTest {
    @Autowired
    SubjectService subjectService;
    @Test
    public void findAllSubjects() throws Exception {
        Assert.assertNotNull(subjectService.findAllSubjects());
    }

    @Test
    public void findSubjectByType() throws Exception {
        Assert.assertNotNull(subjectService.findSubjectByType("JAVA"));
    }

}
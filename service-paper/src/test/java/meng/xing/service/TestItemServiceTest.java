package meng.xing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import meng.xing.ChoiceItem;
import meng.xing.TestItemType;
import meng.xing.entity.TestItem;
import meng.xing.repository.SubjectRepository;
import meng.xing.repository.TestItemRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestItemServiceTest {
    @Autowired
    TestItemRepository testItemRepository;
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    TestItemService testItemService;

    @Test
    public void findTestItemsByTypeAndSubject() throws Exception {

        Assert.assertNotNull(testItemService.findTestItemsByTypeAndSubject("1", null, null));
    }

    @Test
    public void addTestItme() throws Exception {
        Map<String, String> items = new HashMap<>();
        items.put("A", "asda");
        items.put("B", "ds");
        items.put("C", "sd");
        items.put("D", "asdda");
        ChoiceItem choiceItem = new ChoiceItem("题干", items);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(choiceItem);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        TestItem t1 = new TestItem(TestItemType.QUESTION.toString(), "请简述JAVAb编译过程", "答案无");
        TestItem t2 = new TestItem(TestItemType.CHOICE.toString(), json, "A");
        t1.setSubject(subjectRepository.findByType("JAVA"));
        t2.setSubject(subjectRepository.findByType("WEB"));
        Assert.assertEquals(testItemService.addTestItme(t1), true);
        Assert.assertEquals(testItemService.addTestItme(t2), true);
    }

    @Test
    public void updateTestItem() throws Exception {
        TestItem testItem = testItemService.findTestItemById((long) 1);
        testItem.setQuestion("22");
        Assert.assertEquals(testItemService.updateTestItem(testItem), true);
    }

    @Test
    public void deleteTestItemById() throws Exception {
        Assert.assertEquals(testItemService.deleteTestItemById((long) 1), true);
    }

    @Test
    public void findTestItemById() throws Exception {
        Assert.assertNotNull(testItemService.findTestItemById((long) 1));
    }

}
package meng.xing.controller;

import meng.xing.controller.common.RequestIds;
import meng.xing.controller.common.ResponseStatusWithMessage;
import meng.xing.entity.Paper;
import meng.xing.entity.Subject;
import meng.xing.entity.TestItem;
import meng.xing.service.PaperService;
import meng.xing.service.SubjectService;
import meng.xing.service.TestItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/papers")
public class PaperController {
    private final
    PaperService paperService;
    private final
    SubjectService subjectService;
    private final
    TestItemService testItemService;

    @Autowired
    public PaperController(PaperService paperService, SubjectService subjectService, TestItemService testItemService) {
        this.paperService = paperService;
        this.subjectService = subjectService;
        this.testItemService = testItemService;
    }


    @GetMapping
    public Page<Paper> findSomePapers(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "sort", defaultValue = "id") String sort,
                                      @RequestParam(value = "order", defaultValue = "asc") String order,
                                      @RequestParam(value = "subject", required = false) String subject) {
        Subject subjectObj = null;
        if (!Objects.equals(subject, "")) {
            subjectObj = subjectService.findSubjectByType(subject);
        }

        Sort _sort = new Sort(Sort.Direction.fromString(order), sort);
        //传来的页码是从1开始，而服务器从1开始算
        Pageable pageable = new PageRequest(page - 1, pageSize, _sort);
        return paperService.findAllPapersBySubject(subjectObj, pageable);
    }

    @GetMapping("/{id}")
    public ResponsePaper getTestItemsByPaperId(@PathVariable("id") Long id) {
        Paper paper = paperService.findPaperById(id);
        ResponsePaper responsePaper = new ResponsePaper();
        responsePaper.setTestItems(paper.getTestItems());
        return responsePaper;
    }

    @PatchMapping("/{id}")
    public ResponseStatusWithMessage updatePaper(@PathVariable("id") Long id, @RequestBody RequestPaper requestPaper) {
        Subject subject = subjectService.findSubjectByType(requestPaper.getSubject());
        String description = requestPaper.getDescription();
        Set<TestItem> testItems = new HashSet<>();

        if (!requestPaper.getTestItemIds().isEmpty()) {
            requestPaper.getTestItemIds().forEach(testItemId -> testItems.add(testItemService.findTestItemById(testItemId)));
        }
        Paper paper = paperService.findPaperById(id);
        paper.setDescription(description);
        paper.setSubject(subject);
        paper.setTestItems(testItems);
        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();

        if (paperService.updatePaper(paper)) {
            responseStatusWithMessage.setMessage("修改试卷成功");
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        }
        responseStatusWithMessage.setMessage("修改试卷失败");
        responseStatusWithMessage.setSuccess("false");
        return responseStatusWithMessage;
    }

    @PostMapping
    public ResponseStatusWithMessage createPaper(@RequestParam("username") String username, @RequestBody RequestPaper requestPaper) {
        Subject subject = subjectService.findSubjectByType(requestPaper.getSubject());
        String description = requestPaper.getDescription();
        Set<TestItem> testItems = new HashSet<>();

        if (!requestPaper.getTestItemIds().isEmpty()) {
            requestPaper.getTestItemIds().forEach(testItemId -> testItems.add(testItemService.findTestItemById(testItemId)));
        }
        Paper paper = new Paper(username, description, subject, testItems);
        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();
        if (paperService.addPaper(paper)) {
            responseStatusWithMessage.setMessage("新增试卷成功");
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        }
        responseStatusWithMessage.setMessage("新增试卷失败");
        responseStatusWithMessage.setSuccess("false");
        return responseStatusWithMessage;

    }

    @DeleteMapping("/{id}")
    public ResponseStatusWithMessage deleteOnePaper(@PathVariable("id") Long id) {

        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();
        if (paperService.deletePaperById(id)) {
            responseStatusWithMessage.setMessage("删除试卷成功!id:" + id);
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        }
        responseStatusWithMessage.setMessage("删除试卷失败!id:" + id);
        responseStatusWithMessage.setSuccess("false");
        return responseStatusWithMessage;
    }

    @DeleteMapping
    public ResponseStatusWithMessage deletePapers(@RequestBody RequestIds ids) {
        List<Long> _ids = ids.getIds();
        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();
        try {
            _ids.forEach(paperService::deletePaperById);
            responseStatusWithMessage.setMessage("批量删除试卷成功!ids:" + _ids);
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        } catch (Exception e) {
            responseStatusWithMessage.setMessage("批量删除试卷失败!ids:" + _ids);
            responseStatusWithMessage.setSuccess("false");
            return responseStatusWithMessage;
        }


    }

    class RequestPaper {
        private String subject;
        private String description;
        private List<Long> testItemIds;

        public RequestPaper() {
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        List<Long> getTestItemIds() {
            return testItemIds;
        }

        public void setTestItemIds(List<Long> testItemIds) {
            this.testItemIds = testItemIds;
        }
    }

    class ResponsePaper {

        private Set<TestItem> testItems;

        ResponsePaper() {
        }

        public Set<TestItem> getTestItems() {
            return testItems;
        }

        void setTestItems(Set<TestItem> testItems) {
            this.testItems = testItems;
        }
    }

}

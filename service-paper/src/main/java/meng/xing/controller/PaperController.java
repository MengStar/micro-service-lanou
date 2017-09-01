package meng.xing.controller;

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
    public Page<Paper> findAllPaper(@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    @RequestParam(value = "sort", defaultValue = "id") String sort,
                                    @RequestParam(value = "order", defaultValue = "asc") String order,
                                    @RequestParam(value = "subject", required = false) String subject) {
        Subject subjectObj = null;
        if (subject != "") {
            subjectObj = subjectService.findSubjectByType(subject);
        }

        Sort _sort = new Sort(Sort.Direction.fromString(order), sort);
        //传来的页码是从1开始，而服务器从1开始算
        Pageable pageable = new PageRequest(page - 1, pageSize, _sort);
        return paperService.findAllPapersBySubject(subjectObj, pageable);
    }

    @GetMapping("/{id}")
    public Paper getPaperById(@PathVariable("id") Long id) {
        return paperService.findPaperById(id);
    }

    @PatchMapping("/{id}")
    public ResponseStatus update(@PathVariable("id") Long id, @RequestBody RequestPaper requestPaper) {
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
        ResponseStatus responseStatus = new ResponseStatus();

        if (paperService.updatePaper(paper)) {
            responseStatus.setMessage("修改试卷成功");
            responseStatus.setSuccess("true");
            return responseStatus;
        }
        responseStatus.setMessage("修改试卷失败");
        responseStatus.setSuccess("false");
        return responseStatus;
    }

    @PostMapping
    public ResponseStatus createPaper(@RequestParam("username") String username, @RequestBody RequestPaper requestPaper) {
        Subject subject = subjectService.findSubjectByType(requestPaper.getSubject());
        String description = requestPaper.getDescription();
        Set<TestItem> testItems = new HashSet<>();

        if (!requestPaper.getTestItemIds().isEmpty()) {
            requestPaper.getTestItemIds().forEach(testItemId -> testItems.add(testItemService.findTestItemById(testItemId)));
        }
        Paper paper = new Paper(username, description, subject, testItems);
        ResponseStatus responseStatus = new ResponseStatus();
        if (paperService.addPaper(paper)) {
            responseStatus.setMessage("新增试卷成功");
            responseStatus.setSuccess("true");
            return responseStatus;
        }
        responseStatus.setMessage("新增试卷失败");
        responseStatus.setSuccess("false");
        return responseStatus;

    }

    @DeleteMapping("/{id}")
    public ResponseStatus deleteOnePaper(@PathVariable("id") Long id) {

        ResponseStatus responseStatus = new ResponseStatus();
        if (paperService.deletePaperById(id)) {
            responseStatus.setMessage("删除试卷成功!id:" + id);
            responseStatus.setSuccess("true");
            return responseStatus;
        }
        responseStatus.setMessage("删除试卷失败!id:" + id);
        responseStatus.setSuccess("false");
        return responseStatus;
    }

    @DeleteMapping
    public ResponseStatus deletePapers(@RequestBody RequestIds ids) {
        List<Long> _ids = ids.getIds();
        ResponseStatus responseStatus = new ResponseStatus();
        try {
            _ids.forEach(paperService::deletePaperById);
            responseStatus.setMessage("批量删除试卷成功!ids:" + _ids);
            responseStatus.setSuccess("true");
            return responseStatus;
        } catch (Exception e) {
            responseStatus.setMessage("批量删除试卷失败!ids:" + _ids);
            responseStatus.setSuccess("false");
            return responseStatus;
        }


    }
}

class RequestIds {
    private List<Long> ids;

    public RequestIds() {
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getTestItemIds() {
        return testItemIds;
    }

    public void setTestItemIds(List<Long> testItemIds) {
        this.testItemIds = testItemIds;
    }
}

class ResponseStatus {
    private String success;
    private String message;

    public ResponseStatus() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


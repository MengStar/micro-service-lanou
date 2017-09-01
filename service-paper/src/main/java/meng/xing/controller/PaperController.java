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
                                    @RequestParam(value = "subject", defaultValue = "") String subject) {
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
    public Map<String, Object> getPaperById(@PathVariable("id") Long id) {
        Paper _paper = paperService.findPaperById(id);
        Map<String, Object> paper = new HashMap<>();
        paper.put("testItems", _paper.getTestItems());
        return paper;
    }

    @PatchMapping("/{id}")
    public boolean update(@PathVariable("id") Long id, @RequestBody Map<String, Object> map) {
        Subject subject = subjectService.findSubjectByType(map.get("subject").toString());
        String description = map.get("description").toString();
        Set<TestItem> testItems = new HashSet<>();
        if (map.get("testItemIds") != null) {
            ArrayList ids = (ArrayList) map.get("testItemIds");
            ids.forEach(testItemId -> testItems.add(testItemService.findTestItemById((long) (int) testItemId)));
        }
        Paper paper = paperService.findPaperById(id);
        paper.setDescription(description);
        paper.setSubject(subject);
        paper.setTestItems(testItems);
        return paperService.updatePaper(paper);
    }

    @PostMapping
    public boolean create(@RequestParam("username") String username, @RequestBody Map<String, Object> map) {
        Subject subject = subjectService.findSubjectByType(map.get("subject").toString());
        String description = map.get("description").toString();
        Set<TestItem> testItems = new HashSet<>();

        if (map.get("testItemIds") != null) {
            ArrayList ids = (ArrayList) map.get("testItemIds");
            ids.forEach(testItemId -> testItems.add(testItemService.findTestItemById(Long.valueOf((int) testItemId))));
        }
        Paper paper = new Paper(username, description, subject, testItems);
        return paperService.addPaper(paper);

    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        return paperService.deletePaperById(id);
    }

    @DeleteMapping
    public void delete(@RequestBody Map<String, ArrayList<Long>> map) {
        map.get("ids").forEach(paperService::deletePaperById);
    }
}

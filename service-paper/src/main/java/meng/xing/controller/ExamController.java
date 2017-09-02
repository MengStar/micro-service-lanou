package meng.xing.controller;

import meng.xing.controller.common.RequestIds;
import meng.xing.controller.common.ResponseStatusWithMessage;
import meng.xing.entity.Exam;
import meng.xing.entity.Paper;
import meng.xing.entity.Subject;
import meng.xing.service.ExamService;
import meng.xing.service.PaperService;
import meng.xing.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/exams")
public class ExamController {
    private final
    ExamService examService;
    private final
    PaperService paperService;
    private final
    SubjectService subjectService;

    @Autowired
    public ExamController(ExamService examService, PaperService paperService, SubjectService subjectService) {
        this.examService = examService;
        this.paperService = paperService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public Page<Exam> findSomeExams(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                    @RequestParam(value = "sort", defaultValue = "id") String sort,
                                    @RequestParam(value = "order", defaultValue = "asc") String order,
                                    @RequestParam(value = "subject", required = false) String subject) {
        Sort _sort = new Sort(Sort.Direction.fromString(order), sort);
        //传来的页码是从1开始，而服务器从1开始算
        Pageable pageable = new PageRequest(page - 1, pageSize, _sort);
        Subject subjectObj = null;
        if (subject != null) {
            System.out.println(subject);
            subjectObj = subjectService.findSubjectByType(subject);
        }
        return examService.findSomeExamsBySubject(subjectObj, pageable);

    }

    @PatchMapping("/{id}")
    public ResponseStatusWithMessage updatePaper(@PathVariable("id") Long id, @RequestBody RequestExam requestExam) {
        Exam exam = examService.findExamById(id);
        Subject subject = subjectService.findSubjectByType(requestExam.getSubject());
        String description = requestExam.getDescription();
        if (requestExam.getPaperId() != null) {
            Paper paper = paperService.findPaperById(requestExam.getPaperId());
            exam.setPaper(paper);
        }
        exam.setDescription(description);
        exam.setSubject(subject);

        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();
        if (examService.updateExam(exam)) {
            responseStatusWithMessage.setMessage("修改考试成功");
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        }
        responseStatusWithMessage.setMessage("修改考试失败");
        responseStatusWithMessage.setSuccess("false");
        return responseStatusWithMessage;
    }

    @PostMapping
    public ResponseStatusWithMessage createExam(@RequestParam("username") String username, @RequestBody RequestExam requestExam) {
        Subject subject = subjectService.findSubjectByType(requestExam.getSubject());
        String description = requestExam.getDescription();
        Paper paper = null;
        if (requestExam.getPaperId() != null) {
            paper = paperService.findPaperById(requestExam.getPaperId());
        }
        Exam exam = new Exam(description, subject, paper, username);

        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();
        if (examService.addExam(exam)) {
            responseStatusWithMessage.setMessage("新增考试成功");
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        }
        responseStatusWithMessage.setMessage("新增考试失败");
        responseStatusWithMessage.setSuccess("false");
        return responseStatusWithMessage;
    }

    @DeleteMapping("/{id}")
    public ResponseStatusWithMessage delete(@PathVariable("id") Long id) {
        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();
        if (examService.deleteExamById(id)) {
            responseStatusWithMessage.setMessage("删除考试成功");
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        }
        responseStatusWithMessage.setMessage("删除考试失败");
        responseStatusWithMessage.setSuccess("false");
        return responseStatusWithMessage;
    }

    @DeleteMapping
    public ResponseStatusWithMessage delete(@RequestBody RequestIds ids) {
        List<Long> _ids = ids.getIds();
        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();
        try {
            _ids.forEach(examService::deleteExamById);
            responseStatusWithMessage.setMessage("批量删除考试成功!ids:" + _ids);
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        } catch (Exception e) {
            responseStatusWithMessage.setMessage("批量删除考试失败!ids:" + _ids);
            responseStatusWithMessage.setSuccess("false");
            return responseStatusWithMessage;
        }
    }
}

class RequestExam {
    private String subject;
    private String description;
    private Long paperId;

    public RequestExam() {

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

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }
}
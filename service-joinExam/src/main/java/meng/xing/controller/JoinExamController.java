package meng.xing.controller;

import meng.xing.entity.Answer;
import meng.xing.entity.UserDoneExam;
import meng.xing.service.AnswerService;
import meng.xing.service.UserDoneExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/joinExam")
public class JoinExamController {
    private final AnswerService answerService;
    private final UserDoneExamService userDoneExamService;

    @Autowired
    public JoinExamController(AnswerService answerService, UserDoneExamService userDoneExamService) {
        this.answerService = answerService;
        this.userDoneExamService = userDoneExamService;
    }

    @PostMapping("/start")
    public Map<String, Object> start() {
        return null;
    }

    @PostMapping("/end")
    public Map<String, Object> end() {
        return null;
    }

    @PostMapping("/complete")
    public ResponseStatusWithMessage complete(@PathParam("examId") Long examId, @RequestParam("username") String username, @RequestBody List<RequestAnswer> requestAnswerList) {
        ResponseStatusWithMessage responseStatusWithMessage = new ResponseStatusWithMessage();

        if (!userDoneExamService.isExist(username, examId)) {
            userDoneExamService.add(new UserDoneExam(examId, username));
            requestAnswerList.forEach(requestAnswer -> answerService.saveAnswer(new Answer(requestAnswer.getAnswer(), username, requestAnswer.getPaperId(), requestAnswer.getTestItemId())));
            responseStatusWithMessage.setMessage("试卷提交成功");
            responseStatusWithMessage.setSuccess("true");
            return responseStatusWithMessage;
        }
        responseStatusWithMessage.setMessage("你已经完成过该试卷，不能再提交");
        responseStatusWithMessage.setSuccess("false");
        return responseStatusWithMessage;
    }

    @GetMapping("/getAnswer")
    public List<Answer> getAnswerByUsernameAndPaperId(@PathParam("paperId") Long paperId, @PathParam("username") String username) {
        return answerService.findAnswerByUsernameAndPaperId(username, paperId);
    }

    static class RequestAnswer {

        private String answer;
        private Long paperId;
        private Long testItemId;

        public RequestAnswer() {

        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public Long getPaperId() {
            return paperId;
        }

        public void setPaperId(Long paperId) {
            this.paperId = paperId;
        }

        public Long getTestItemId() {
            return testItemId;
        }

        public void setTestItemId(Long testItemId) {
            this.testItemId = testItemId;
        }
    }

    static class ResponseStatusWithMessage {
        private String success;
        private String message;

        public ResponseStatusWithMessage() {
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
}

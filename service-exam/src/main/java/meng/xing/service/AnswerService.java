package meng.xing.service;

import meng.xing.entity.Answer;

import java.util.List;

public interface AnswerService {
    List<Answer> findAnswerByUsernameAndPaperId(String username, Long paperId);

    boolean saveAnswer(Answer answer);
}

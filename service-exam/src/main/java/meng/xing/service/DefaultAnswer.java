package meng.xing.service;

import meng.xing.entity.Answer;
import meng.xing.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultAnswer implements AnswerService {
    private final AnswerRepository answerRepository;

    @Autowired
    public DefaultAnswer(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public List<Answer> findAnswerByUsernameAndPaperId(String username, Long paperId) {
        return answerRepository.findByUsernameAndPaperId(username, paperId);
    }

    @Override
    public boolean saveAnswer(Answer answer) {
        return answerRepository.save(answer) != null;
    }
}

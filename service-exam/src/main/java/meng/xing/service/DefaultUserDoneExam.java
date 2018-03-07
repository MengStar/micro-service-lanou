package meng.xing.service;

import meng.xing.entity.UserDoneExam;
import meng.xing.repository.UserDoneExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultUserDoneExam implements UserDoneExamService {
    private final
    UserDoneExamRepository userDoneExamRepository;

    @Autowired
    public DefaultUserDoneExam(UserDoneExamRepository userDoneExamRepository) {
        this.userDoneExamRepository = userDoneExamRepository;
    }

    @Override
    public boolean add(UserDoneExam userDoneExam) {
        return userDoneExamRepository.save(userDoneExam) != null;
    }

    @Override
    public boolean isExist(String username, Long examId) {
        return userDoneExamRepository.findByUsernameAndExamId(username, examId) != null;
    }


}

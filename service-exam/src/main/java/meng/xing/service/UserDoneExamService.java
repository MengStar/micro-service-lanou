package meng.xing.service;

import meng.xing.entity.UserDoneExam;

public interface UserDoneExamService {
    boolean add(UserDoneExam userDoneExam);

    boolean isExist(String username, Long examId);
}

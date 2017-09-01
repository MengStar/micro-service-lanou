package meng.xing.service;

import meng.xing.entity.Subject;

import java.util.List;

public interface SubjectService {
    List<Subject> findAllSubjects();
    Subject findSubjectByType(String type);
}

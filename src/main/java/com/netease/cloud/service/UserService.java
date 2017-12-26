package com.netease.cloud.service;

import com.netease.cloud.dao.po.StudentPo;

public interface UserService {
    StudentPo selectStudent(String name);
    int insertStudent(String name);
    void insertStudentGrade(int studentId,String grade);
    void deleteAll();
}

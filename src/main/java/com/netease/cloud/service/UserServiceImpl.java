package com.netease.cloud.service;

import com.netease.cloud.dao.mapper.StudentGradeMapper;
import com.netease.cloud.dao.mapper.StudentMapper;
import com.netease.cloud.dao.po.StudentGradePo;
import com.netease.cloud.dao.po.StudentPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private StudentMapper studentMapper;
@Autowired
private StudentGradeMapper studentGradeMapper;
    @Override
    public StudentPo selectStudent(String name) {

        return studentMapper.selectStudent(name);
    }

    @Override
    public int insertStudent(String name) {
        StudentPo studentPo=new StudentPo();
        studentPo.setName(name);
        studentMapper.insertStudent(studentPo);
        return studentPo.getId();
    }

    @Override
    public void insertStudentGrade(int studentId, String grade) {
        StudentGradePo studentGradePo=new StudentGradePo();
        studentGradePo.setStudentId(studentId);
        studentGradePo.setGrade(grade);
        studentGradeMapper.insertStudentGrade(studentGradePo);
    }

    @Override
    public void deleteAll() {
        studentMapper.deleteAll();
    }
}

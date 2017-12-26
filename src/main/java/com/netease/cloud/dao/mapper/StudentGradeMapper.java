package com.netease.cloud.dao.mapper;

import com.netease.cloud.dao.po.StudentGradePo;
import com.netease.cloud.dao.po.StudentPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGradeMapper {
//    StudentPo selectStudentGrade(String studentId);
    void insertStudentGrade(StudentGradePo studentGradePo);
//    void deleteAll();
}

package com.netease.cloud.dao.mapper;

import com.netease.cloud.dao.po.StudentPo;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentMapper {
    StudentPo selectStudent(String name);
    void insertStudent(StudentPo studentPo);
    void deleteAll();
}

package com.netease.cloud;

import com.netease.cloud.dao.dynamic.DataSourceContextHolder;
import com.netease.cloud.dao.dynamic.DbType;
import com.netease.cloud.dao.dynamic.DynamicDataSource;
import com.netease.cloud.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Random;

public class MainStart {
    public static void main(String[] args) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        UserService userService= (UserService) applicationContext.getBean("userServiceImpl");
//        userService.deleteAll();
        int i=300000;
        for (;i<1000000;i++){
//            DataSourceContextHolder.setDbType(DbType.db_admin.name());
            int id=userService.insertStudent("stu"+i);
            userService.insertStudentGrade(id,"grade"+new Random().nextInt(10));
//            DataSourceContextHolder.setDbType(DbType.db_slave.name());
//            System.out.println(userService.selectStudent("jgc2test"+i));
        }

    }
}

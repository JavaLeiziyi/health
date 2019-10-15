package com.lzy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lzy.Service.StudentService;
import com.lzy.entity.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Reference(loadbalance = "random")
    private StudentService studentService;

    @RequestMapping("/findAll")
    public List<Student> findAll(){
        List<Student> studentList = studentService.findAll();
        return studentList;
    }

}

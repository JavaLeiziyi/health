package com.lzy.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.lzy.Service.StudentService;
import com.lzy.entity.Course;
import com.lzy.entity.Grade;
import com.lzy.entity.Student;
import com.lzy.mapper.CourseMapper;
import com.lzy.mapper.GradeMapper;
import com.lzy.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service(interfaceClass = StudentService.class)
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public List<Student> findAll(){
        List<Student> studentList = studentMapper.findAll();
        for (Student student : studentList) {
            Grade grade = gradeMapper.findBySid(student.getCid());
            List<Course> courseList = courseMapper.findBySid(student.getId());
            student.setGrade(grade);
            student.setCourseList(courseList);
        }

        return studentList;
    }
}

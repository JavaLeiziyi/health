package com.lzy.mapper;

import com.lzy.entity.Course;

import java.util.List;

public interface CourseMapper {

    List<Course> findBySid(int sid);
}

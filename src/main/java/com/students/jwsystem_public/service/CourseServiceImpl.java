package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.CourseMapper;
import com.students.jwsystem_public.mapper.CourseSelectionMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    //：注入 CourseSelectionMapper
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    //注入 UserMapper
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Course> getAllCourses() {
        return courseMapper.getAllCourses();
    }

    @Override
    public Course getCourseById(Long id) {
        return courseMapper.getCourseById(id);
    }


    @Override
    public List<Course> getAllCourses(String username) {
        // 1. 先查出所有课程
        List<Course> courses = courseMapper.getAllCourses();

        // 2. 如果用户已登录，查出该学生已选的课程ID列表
        if (username != null && !username.isEmpty()) {
            User user = userMapper.findByUsername(username);
            if (user != null) {
                // 查询已选课程ID列表
                List<Long> selectedCourseIds =
                        courseSelectionMapper.selectCourseIdsByStudentId(user.getId());
                // 转成 Set，方便快速判断
                Set<Long> selectedSet = new HashSet<>(selectedCourseIds);

                // 3. 遍历每门课，打上 isSelected 标记
                for (Course course : courses) {
                    course.setIsSelected(selectedSet.contains(course.getId()));
                }
            }
        }
        return courses;
    }
}
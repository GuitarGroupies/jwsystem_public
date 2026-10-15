package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.User;                       // ✅ 新增
import com.students.jwsystem_public.mapper.CourseMapper;
import com.students.jwsystem_public.mapper.CourseSelectionMapper;      // ✅ 新增
import com.students.jwsystem_public.mapper.UserMapper;                 // ✅ 新增
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;   // ✅ 新增
import java.util.List;
import java.util.Set;       // ✅ 新增

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    // ✅ 新增：注入 CourseSelectionMapper
    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    // ✅ 新增：注入 UserMapper
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

    // ✅ 修改：原来直接 return List.of();，现在真正实现
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
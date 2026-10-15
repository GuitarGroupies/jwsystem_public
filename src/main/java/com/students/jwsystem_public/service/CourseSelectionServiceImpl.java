package com.students.jwsystem_public.service.impl;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSelection;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.CourseMapper;
import com.students.jwsystem_public.mapper.CourseSelectionMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import com.students.jwsystem_public.service.CourseSelectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;   // ✅ 新增

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired
    private CourseSelectionMapper courseSelectionMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public String selectCourse(Long courseId, String username) {
        User user = userMapper.findByUsername(username);
        Long studentId = user.getId();

        if (courseSelectionMapper.countByStudentAndCourse(studentId, courseId) > 0) {
            return "您已选过该课程";
        }

        Course course = courseMapper.getCourseById(courseId);
        if (course == null) {
            return "课程不存在";
        }

        if (course.getSelectedCount() >= course.getCapacity()) {
            return "课程已满";
        }

        CourseSelection cs = new CourseSelection();
        cs.setStudentId(studentId);
        cs.setCourseId(courseId);
        courseSelectionMapper.insertSelection(cs);
        courseSelectionMapper.incrementSelectedCount(courseId);

        return "选课成功";
    }

    @Override
    @Transactional
    public String dropCourse(Long courseId, String username) {
        User user = userMapper.findByUsername(username);
        Long studentId = user.getId();

        int rows = courseSelectionMapper.deleteSelection(studentId, courseId);
        if (rows == 0) {
            return "您没有选这门课";
        }
        courseSelectionMapper.decrementSelectedCount(courseId);
        return "退课成功";
    }

    @Override
    @Transactional
    public void dropCourse(Long studentId, Long courseId) {
        int count = courseSelectionMapper.countByStudentAndCourse(studentId, courseId);
        if (count == 0) {
            throw new RuntimeException("您还未选择该课程，无法退课");
        }
        courseSelectionMapper.deleteSelection(studentId, courseId);
        courseSelectionMapper.decrementSelectedCount(courseId);
    }

    // ✅ 新增：查询当前学生已选课程列表
    @Override
    public List<Course> getMyCourses(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return courseSelectionMapper.selectMyCourses(user.getId());
    }
}
package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSelection;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.CourseMapper;
import com.students.jwsystem_public.mapper.CourseSelectionMapper;
import com.students.jwsystem_public.mapper.ScoreMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired private CourseSelectionMapper courseSelectionMapper;
    @Autowired private CourseMapper courseMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private ScoreMapper scoreMapper; // ✅ 新增注入

    @Override
    @Transactional
    public String selectCourse(Long courseId, String username) {
        // ... 保持你原来的代码不变
        User user = userMapper.findByUsername(username);
        Long studentId = user.getId();
        if (courseSelectionMapper.countByStudentAndCourse(studentId, courseId) > 0) return "您已选过该课程";
        Course course = courseMapper.getCourseById(courseId);
        if (course == null) return "课程不存在";
        if (course.getSelectedCount() >= course.getCapacity()) return "课程已满";
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
        // ... 保持你原来的代码不变
        User user = userMapper.findByUsername(username);
        Long studentId = user.getId();
        int rows = courseSelectionMapper.deleteSelection(studentId, courseId);
        if (rows == 0) return "您没有选这门课";
        courseSelectionMapper.decrementSelectedCount(courseId);
        return "退课成功";
    }

    @Override
    @Transactional
    public void dropCourse(Long studentId, Long courseId) {
        // ... 保持你原来的代码不变
        int count = courseSelectionMapper.countByStudentAndCourse(studentId, courseId);
        if (count == 0) throw new RuntimeException("您还未选择该课程，无法退课");
        courseSelectionMapper.deleteSelection(studentId, courseId);
        courseSelectionMapper.decrementSelectedCount(courseId);
    }

    @Override
    public List<Course> getMyCourses(String username) {
        // ... 保持你原来的代码不变
        User user = userMapper.findByUsername(username);
        if (user == null) throw new RuntimeException("用户不存在");
        return courseSelectionMapper.selectMyCourses(user.getId());
    }

    @Override
    public List<CourseSelection> getStudentsByCourseId(Long courseId) {
        return courseSelectionMapper.selectStudentsByCourseId(courseId);
    }

    @Override
    public List<CourseSelection> getAllStudentsByTeacher(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new RuntimeException("用户不存在");
        return courseSelectionMapper.selectAllStudentsByTeacherId(user.getId());
    }

    // ✅ 核心逻辑：将学生移出当前老师的所有课程
    @Override
    @Transactional
    public void removeStudentFromTeacherCourses(Long studentId, String teacherUsername) {
        User teacher = userMapper.findByUsername(teacherUsername);
        if (teacher == null) throw new RuntimeException("教师不存在");

        // 1. 查出这个老师教的所有课程
        List<Course> teacherCourses = courseMapper.selectByTeacherId(teacher.getId());

        // 2. 遍历这些课程，精准移出该学生
        for (Course course : teacherCourses) {
            Long courseId = course.getId();

            // 删除该学生在这门课的成绩
            scoreMapper.deleteByStudentAndCourse(studentId, courseId);

            // 删除选课记录，并判断是否真的删除了
            int rows = courseSelectionMapper.deleteSelection(studentId, courseId);

            // 如果确实删除了，该课程人数减一
            if (rows > 0) {
                courseSelectionMapper.decrementSelectedCount(courseId);
            }
        }
    }
}
package com.students.jwsystem_public.service;

import cn.hutool.json.JSONUtil;
import com.students.jwsystem_public.entity.Course;
import com.students.jwsystem_public.entity.CourseSelection;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.CourseMapper;
import com.students.jwsystem_public.mapper.CourseSelectionMapper;
import com.students.jwsystem_public.mapper.ScoreMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CourseSelectionServiceImpl implements CourseSelectionService {

    @Autowired private CourseSelectionMapper courseSelectionMapper;
    @Autowired private CourseMapper courseMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private ScoreMapper scoreMapper;
    @Autowired private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public String selectCourse(Long courseId, String username) {
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

        // ✅ 删缓存
        redisTemplate.delete("course:my:" + studentId);
        redisTemplate.delete("schedule:my:" + studentId);

        return "选课成功";
    }

    @Override
    @Transactional
    public String dropCourse(Long courseId, String username) {
        User user = userMapper.findByUsername(username);
        Long studentId = user.getId();

        int rows = courseSelectionMapper.deleteSelection(studentId, courseId);
        if (rows == 0) return "您没有选这门课";
        courseSelectionMapper.decrementSelectedCount(courseId);

        // ✅ 删缓存
        redisTemplate.delete("course:my:" + studentId);
        redisTemplate.delete("schedule:my:" + studentId);

        return "退课成功";
    }

    @Override
    @Transactional
    public void dropCourse(Long studentId, Long courseId) {
        int count = courseSelectionMapper.countByStudentAndCourse(studentId, courseId);
        if (count == 0) throw new RuntimeException("您还未选择该课程，无法退课");

        courseSelectionMapper.deleteSelection(studentId, courseId);
        courseSelectionMapper.decrementSelectedCount(courseId);

        // ✅ 删缓存
        redisTemplate.delete("course:my:" + studentId);
        redisTemplate.delete("schedule:my:" + studentId);
    }

    @Override
    public List<Course> getMyCourses(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new RuntimeException("用户不存在");

        String redisKey = "course:my:" + user.getId();

        // 1. 查 Redis
        Object cachedData = redisTemplate.opsForValue().get(redisKey);
        if (cachedData != null) {
            System.out.println("=== 选课列表命中缓存，直接返回 ===");
            // ✅ 手动转 JSON
            return JSONUtil.toList(cachedData.toString(), Course.class);
        }

        // 2. 查 MySQL
        List<Course> courses = courseSelectionMapper.selectMyCourses(user.getId());
        System.out.println("=== 选课列表走数据库查询 ===");

        // 3. 写缓存
        redisTemplate.opsForValue().set(redisKey, JSONUtil.toJsonStr(courses), 30, TimeUnit.MINUTES);

        return courses;
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

    @Override
    @Transactional
    public void removeStudentFromTeacherCourses(Long studentId, String teacherUsername) {
        User teacher = userMapper.findByUsername(teacherUsername);
        if (teacher == null) throw new RuntimeException("教师不存在");

        List<Course> teacherCourses = courseMapper.selectByTeacherId(teacher.getId());

        for (Course course : teacherCourses) {
            Long courseId = course.getId();
            scoreMapper.deleteByStudentAndCourse(studentId, courseId);
            int rows = courseSelectionMapper.deleteSelection(studentId, courseId);
            if (rows > 0) {
                courseSelectionMapper.decrementSelectedCount(courseId);
            }
        }

        // ✅ 删缓存
        redisTemplate.delete("course:my:" + studentId);
        redisTemplate.delete("schedule:my:" + studentId);
    }
}
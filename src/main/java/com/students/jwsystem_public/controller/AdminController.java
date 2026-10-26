package com.students.jwsystem_public.controller;

import com.students.jwsystem_public.entity.*;
import com.students.jwsystem_public.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired private CourseMapper courseMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private ScoreMapper scoreMapper;
    @Autowired private CourseSelectionMapper courseSelectionMapper;
    @Autowired private CourseScheduleMapper courseScheduleMapper;
    @Autowired private BasicDataMapper basicDataMapper;

    @GetMapping("/stats")
    public Map<String,Object> getStats(){
        Map<String,Object> result = new HashMap<>();
        result.put("studentCount",userMapper.countByRole("STUDENT"));
        result.put("teacherCount",userMapper.countByRole("TEACHER"));
        result.put("courseCount", courseMapper.countAllCourses());
        return result;
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){ return userMapper.findAll(); }

    @PostMapping("/reset-password/{id}")
    public Map<String, Object> resetPassword(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try { userMapper.resetPassword(id, "123456"); result.put("success", true); result.put("message", "密码已重置为 123456"); }
        catch (Exception e) { result.put("success", false); result.put("message", e.getMessage()); }
        return result;
    }

    @GetMapping("/courses")
    public List<Course> getAllCourses() { return courseMapper.getAllCourses(); }

    @GetMapping("/teachers")
    public List<User> getAllTeachers() { return userMapper.findAllTeachers(); }

    @PostMapping("/add-course")
    public Map<String, Object> addCourse(@RequestBody Course course) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 1. 插入课程
            courseMapper.insertCourse(course);

            // 2. 如果是必修课，自动给目标班级选课
            if ("必修".equals(course.getCourseType()) && course.getTargetClassId() != null) {
                courseMapper.batchInsertSelection(course.getTargetClassId(), course.getId());
                courseMapper.updateCourseSelectedCount(course.getId());
            }

            result.put("success", true);
            result.put("message", "课程添加成功" + ("必修".equals(course.getCourseType()) ? "，已自动为该班级分配" : ""));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "添加失败: " + e.getMessage());
        }
        return result;
    }

    @PostMapping("/delete-course/{id}")
    @Transactional
    public Map<String, Object> deleteCourse(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            scoreMapper.deleteByCourseId(id);
            courseSelectionMapper.deleteByCourseId(id);
            courseScheduleMapper.deleteByCourseId(id);
            courseMapper.deleteCourse(id);
            result.put("success", true); result.put("message", "删除成功");
        } catch (Exception e) { result.put("success", false); result.put("message", "删除失败: " + e.getMessage()); }
        return result;
    }

    @GetMapping("/depts")
    public List<Department> getDepts() { return basicDataMapper.findAllDepts(); }

    @PostMapping("/add-dept")
    public Map<String, Object> addDept(@RequestBody Map<String, String> params) {
        try { basicDataMapper.insertDept(params.get("deptName")); return Map.of("success", true, "message", "添加成功"); }
        catch (Exception e) { return Map.of("success", false, "message", e.getMessage()); }
    }

    @GetMapping("/majors")
    public List<Major> getMajors(@RequestParam Long deptId) { return basicDataMapper.findMajorsByDeptId(deptId); }

    @PostMapping("/add-major")
    public Map<String, Object> addMajor(@RequestBody Map<String, String> params) {
        try { basicDataMapper.insertMajor(params.get("majorName"), Long.valueOf(params.get("deptId"))); return Map.of("success", true, "message", "添加成功"); }
        catch (Exception e) { return Map.of("success", false, "message", e.getMessage()); }
    }

    @GetMapping("/classes")
    public List<SysClass> getClasses(@RequestParam Long majorId) { return basicDataMapper.findClassesByMajorId(majorId); }

    @PostMapping("/add-class")
    public Map<String, Object> addClass(@RequestBody Map<String, String> params) {
        try { basicDataMapper.insertClass(params.get("className"), Long.valueOf(params.get("majorId"))); return Map.of("success", true, "message", "添加成功"); }
        catch (Exception e) { return Map.of("success", false, "message", e.getMessage()); }
    }

    @GetMapping("/teachers/by-dept")
    public List<User> getTeachersByDept(@RequestParam Long deptId) {
        return userMapper.findTeachersByDeptId(deptId);
    }

    @GetMapping("/classes/by-dept")
    public List<SysClass> getClassesByDept(@RequestParam Long deptId) {
        return basicDataMapper.findClassesByDeptId(deptId);
    }

    @GetMapping("/students/by-class")
    public List<User> getStudentsByClass(@RequestParam Long classId) {
        return userMapper.findStudentsByClassId(classId);
    }

    @PostMapping("/update-dept")
    public Map<String, Object> updateDept(@RequestBody Map<String, String> params) {
        try { basicDataMapper.updateDept(Long.valueOf(params.get("id")), params.get("deptName")); return Map.of("success", true, "message", "修改成功"); }
        catch (Exception e) { return Map.of("success", false, "message", e.getMessage()); }
    }


    @PostMapping("/update-major")
    public Map<String, Object> updateMajor(@RequestBody Map<String, String> params) {
        try { basicDataMapper.updateMajor(Long.valueOf(params.get("id")), params.get("majorName")); return Map.of("success", true, "message", "修改成功"); }
        catch (Exception e) { return Map.of("success", false, "message", e.getMessage()); }
    }


    @PostMapping("/update-class")
    public Map<String, Object> updateClass(@RequestBody Map<String, String> params) {
        try { basicDataMapper.updateClass(Long.valueOf(params.get("id")), params.get("className")); return Map.of("success", true, "message", "修改成功"); }
        catch (Exception e) { return Map.of("success", false, "message", e.getMessage()); }
    }
}
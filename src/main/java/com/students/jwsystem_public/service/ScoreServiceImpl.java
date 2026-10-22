package com.students.jwsystem_public.service.impl;

import com.students.jwsystem_public.entity.Score;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.ScoreMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import com.students.jwsystem_public.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    @Autowired
    private ScoreMapper scoreMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Score> getMyScores(String username) {
        User user = userMapper.findByUsername(username);
        if (user == null) throw new RuntimeException("用户不存在");
        return scoreMapper.selectByStudentId(user.getId());
    }


    @Override
    @Transactional
    public boolean saveOrUpdateScore(Long studentId, Long courseId, Double scoreValue) {
        if (scoreValue == null || scoreValue < 0 || scoreValue > 100) {
            throw new RuntimeException("分数必须在 0-100 之间");
        }

        Score existing = scoreMapper.selectByStudentAndCourse(studentId, courseId);
        BigDecimal scoreDecimal = BigDecimal.valueOf(scoreValue);

        if (existing != null) {
            // 更新
            existing.setScore(scoreDecimal);
            return scoreMapper.updateScore(existing) > 0;
        } else {
            // 新增
            Score newScore = new Score();
            newScore.setStudentId(studentId);
            newScore.setCourseId(courseId);
            newScore.setScore(scoreDecimal);
            return scoreMapper.insertScore(newScore) > 0;
        }
    }
}
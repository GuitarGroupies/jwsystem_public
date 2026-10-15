package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Score;
import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.ScoreMapper;
import com.students.jwsystem_public.mapper.UserMapper;
import com.students.jwsystem_public.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return scoreMapper.selectByStudentId(user.getId());
    }
}
package com.students.jwsystem_public.service;

import com.students.jwsystem_public.entity.Score;
import java.util.List;

public interface ScoreService {
    List<Score> getMyScores(String username);
}
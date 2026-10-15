package com.students.jwsystem_public.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 作用：无论后端发生什么异常，都统一返回 JSON，前端不会再出现 "Unexpected end of JSON input"
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Map<String, Object> handleAll(Exception e) {
        e.printStackTrace(); // 控制台仍然打印完整堆栈，方便排查
        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("message", e.getMessage() == null ? "服务器内部错误" : e.getMessage());
        return result;
    }
}
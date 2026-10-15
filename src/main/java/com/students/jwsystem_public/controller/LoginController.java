package com.students.jwsystem_public.controller;

import com.students.jwsystem_public.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        // 1. 获取前端传来的参数
        String username = params.get("username");
        String password = params.get("password");
        String captcha = params.get("captcha");   // 用户输入的验证码
        String key = params.get("key");           // 验证码的 key

        // 2. 校验验证码
        String code = CaptchaController.CAPTCHA_MAP.get(key);
        if (code == null || !code.equalsIgnoreCase(captcha)) {
            return Map.of("success", false, "message", "验证码错误");
        }
        CaptchaController.CAPTCHA_MAP.remove(key); // 验证码一次性使用，用完即删

        // 3. 使用 Spring Security 进行认证（用户名 + 密码）
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4. 获取用户角色
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        // 5. 生成 JWT Token
        String token = JwtUtil.generateToken(username, role);

        // 6. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("token", token);
        result.put("username", username);
        result.put("role", role);
        return result;
    }
}
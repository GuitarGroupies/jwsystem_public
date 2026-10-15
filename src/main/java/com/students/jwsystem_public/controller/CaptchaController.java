package com.students.jwsystem_public.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {
    public  static final Map<String, String> CAPTCHA_MAP = new ConcurrentHashMap<>();
    @GetMapping
    public Map<String, String> getCaptcha() {
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(120,40,4,20);
        // 获取验证码文本
        String code = captcha.getCode();
        // 生成唯一 key
        String key = UUID.randomUUID().toString();


        CAPTCHA_MAP.put(key,code);

        //返回给前端
        Map<String, String> result = new HashMap<>();
        result.put("key",key);
        result.put("image","data:image/png;base64,"+captcha.getImageBase64());
        return result;
    }




}

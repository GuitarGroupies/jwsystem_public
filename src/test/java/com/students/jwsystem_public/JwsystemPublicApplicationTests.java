package com.students.jwsystem_public;

import com.students.jwsystem_public.entity.User;
import com.students.jwsystem_public.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwsystemPublicApplicationTests {
    @Autowired
    private UserMapper userMapper;


    @Test
    void testFIndByUsername() {
        User user = userMapper.findByUsername("admin");
        System.out.println(user);

    }


}

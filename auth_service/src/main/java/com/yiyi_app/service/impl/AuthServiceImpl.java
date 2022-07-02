package com.yiyi_app.service.impl;

import com.yiyi_app.entity.User;
import com.yiyi_app.mapper.UserMapper;
import com.yiyi_app.service.AuthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    UserMapper userMapper;

    @Override
    public String getPwdByUsername(String username) {
        return userMapper.getPasswordByUsername(username);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserDetailByUsername(username);
    }
}

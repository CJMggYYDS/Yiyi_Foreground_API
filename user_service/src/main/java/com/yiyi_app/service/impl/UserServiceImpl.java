package com.yiyi_app.service.impl;

import com.yiyi_app.entity.User;
import com.yiyi_app.mapper.ProfileMapper;
import com.yiyi_app.mapper.UserMapper;
import com.yiyi_app.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    ProfileMapper profileMapper;

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public Boolean updateUserDetails(User user) {
        return userMapper.updateUser(user) > 0; // >0表示更新成功
    }

    @Override
    public Boolean insertNewUser(User user) {
        return userMapper.insertUser(user) > 0;
    }

    @Override
    public User getUserByUID(String uid) {
        return userMapper.getUserByUID(uid);
    }
}

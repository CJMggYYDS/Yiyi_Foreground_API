package com.yiyi_app.service;

import com.yiyi_app.entity.User;

public interface AuthService {

    String getPwdByUsername(String username);

    User getUserByUsername(String username);
}

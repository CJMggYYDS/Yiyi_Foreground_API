package com.yiyi_app.service;

import com.yiyi_app.entity.User;

public interface UserService {

    User getUserByUsername(String username);

    User getUserByUID(String uid);

    Boolean updateUserDetails(User user);

    Boolean insertNewUser(User user);

}

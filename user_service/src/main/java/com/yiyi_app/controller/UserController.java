package com.yiyi_app.controller;

import com.yiyi_app.entity.User;
import com.yiyi_app.service.UserService;
import com.yiyi_app.util.ResponseCodeEnum;
import com.yiyi_app.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
public class UserController {

    @Resource
    UserService userService;

    /**
     *  用户注册
     * @author cjm
     * @date: 2022/7/2
     * @param user(username, password...)
     */
    @PostMapping("/users/register")
    public ResponseResult registerNewUser(@RequestBody User user) {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString();
        user.setUid(uid);
        boolean res = userService.insertNewUser(user);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
     * 获取用户个人信息
     * @author cjm
     * @date: 2022/7/2
     * @param uid
     */
    @GetMapping("/users/{uid}")
    public ResponseResult getUserDetail(@PathVariable("uid") String uid) {
        User user = userService.getUserByUID(uid);
        if(user!=null) {
            return ResponseResult.success(user);
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
     * 检查用户名是否重复
     * @author cjm
     * @date: 2022/7/2
     * @param username
     */
    @GetMapping("/users/check/{username}")
    public ResponseResult checkUsername(@PathVariable("username") String username) {
        User user=userService.getUserByUsername(username);
        if(user==null) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error(ResponseCodeEnum.USER_EXISTED.getCode(), ResponseCodeEnum.USER_EXISTED.getMsg());
        }
    }

    /**
     * 获取收藏夹物品
     * @author cjm
     * @date: 2022/7/2
     * @param username
     */

}

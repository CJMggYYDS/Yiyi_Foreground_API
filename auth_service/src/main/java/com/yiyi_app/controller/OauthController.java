package com.yiyi_app.controller;

import com.yiyi_app.entity.User;
import com.yiyi_app.service.AuthService;
import com.yiyi_app.util.JWTUtil;
import com.yiyi_app.util.ResponseCodeEnum;
import com.yiyi_app.util.ResponseResult;
import com.yiyi_app.vo.LoginResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
public class OauthController {

    @Resource
    AuthService authService;

    @Value("${secretKey:yiyi_app}")
    String secretKey;

    /**
     * 登录并获取token
     *
     * @param userInfo
     * @return ResponseResult
     */
    @PostMapping("/auth/token")
    public ResponseResult login(@RequestBody Map<String, String> userInfo) {
        String username = userInfo.get("username");
        String password = userInfo.get("password");

        User user = authService.getUserByUsername(username);

        if(user!=null) {
            if(password.equals(user.getPassword())) {
                String token = JWTUtil.createJwtToken(user.getUid(), secretKey);
                LoginResponse response=new LoginResponse(user.getUid(), token);
                return ResponseResult.success(response);
            }
            else {
                return ResponseResult.error(ResponseCodeEnum.PASSWORD_ERROR.getCode(), ResponseCodeEnum.PASSWORD_ERROR.getMsg());
            }
        }
        else {
            return ResponseResult.error(ResponseCodeEnum.LOGIN_ERROR.getCode(), ResponseCodeEnum.LOGIN_ERROR.getMsg());
        }
    }
}

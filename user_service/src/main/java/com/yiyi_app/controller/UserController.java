package com.yiyi_app.controller;

import com.yiyi_app.vo.ItemTimeVO;
import com.yiyi_app.vo.ItemVO;
import com.yiyi_app.entity.User;
import com.yiyi_app.service.LogService;
import com.yiyi_app.service.UserService;
import com.yiyi_app.util.ResponseCodeEnum;
import com.yiyi_app.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {

    @Resource
    UserService userService;

    @Resource
    LogService logger;

    /**
     *  用户注册
     * @author cjm
     * @date: 2022/7/2
     * @param registerInfo
     */
    @PostMapping("/users/register")
    public ResponseResult registerNewUser(@RequestBody Map<String, String> registerInfo) {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString();
        User user=new User();
        user.setUid(uid);
        user.setUsername(registerInfo.get("username"));
        user.setPassword(registerInfo.get("password"));
        user.setStatus(1);
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
     * 修改用户信息
     * @author cjm
     * @date: 2022/7/2
     */
    @PutMapping("/users/update")
    public ResponseResult updateUserDetails(@RequestBody User user) {
        boolean res=userService.updateUserDetails(user);
        if(res) {
            return ResponseResult.success();
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
     * @param uid
     */
    @GetMapping("/users/favourite/{uid}")
    public ResponseResult getProfileItems(@PathVariable("uid") String uid) {
        List<ItemVO> responseData=userService.getItemsFromProfile(uid);
        return ResponseResult.success(responseData);
    }

    /**
     * 将物品添加至收藏夹
     * @author cjm
     * @date: 2022/7/2
     */
    @PostMapping("/users/favourite")
    public ResponseResult addItemToProfile(@RequestBody Map<String, String> info) {
        String uid=info.get("uid");
        String itemId=info.get("itemId");
        boolean res=userService.addItemIntoProfile(uid, itemId);
        if(res) {
            logger.addProfileAction(uid, itemId);
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
     * 将物品从收藏夹中移除
     * @author cjm
     * @date: 2022/7/2
     */
    @DeleteMapping("/users/favourite")
    public ResponseResult removeItem(@RequestBody Map<String, String> info) {
        String uid=info.get("uid");
        String itemId=info.get("itemId");
        boolean res=userService.removeItemFromProfile(uid, itemId);
        if(res) {
            logger.removeProfileAction(uid, itemId);
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
     *  获得历史浏览记录
     * @author cjm
     * @date: 2022/7/5
     */
    @GetMapping("/users/history/{uid}")
    public ResponseResult getLogItems(@PathVariable("uid") String uid) {
        List<ItemTimeVO> responseData=userService.getItemsFromLog(uid);
        return ResponseResult.success(responseData);
    }
}

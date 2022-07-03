package com.yiyi_app.service.impl;

import com.yiyi_app.entity.Item;
import com.yiyi_app.entity.Profile;
import com.yiyi_app.entity.User;
import com.yiyi_app.mapper.ProfileMapper;
import com.yiyi_app.mapper.UserMapper;
import com.yiyi_app.service.UserService;
import com.yiyi_app.service.client.ItemClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    ProfileMapper profileMapper;

    @Resource
    ItemClient itemClient;

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

    @Override
    public List<Profile> getProfiles(String uid) {
        return profileMapper.getProfileByUserId(uid);
    }

    @Override
    public Boolean addItemIntoProfile(String uid, String itemId) {
        return profileMapper.insertProfileByUserIdAndItemId(uid, itemId) > 0;
    }

    @Override
    public Boolean removeItemFromProfile(String uid, String itemId) {
        return profileMapper.deleteProfileByUserIdAndItemId(uid, itemId) > 0;
    }

    @Override
    public List<Item> getItemsFromProfile(String uid) {
        List<Profile> profiles=getProfiles(uid);
        List<Item> items=new ArrayList<>();
        profiles.forEach(profile -> {
            Item item=itemClient.getItemByItemId(profile.getItemId());
            items.add(item);
        });
        return items;
    }
}

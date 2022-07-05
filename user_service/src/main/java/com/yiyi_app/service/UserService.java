package com.yiyi_app.service;

import com.yiyi_app.entity.Item;
import com.yiyi_app.entity.Profile;
import com.yiyi_app.entity.User;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);

    User getUserByUID(String uid);

    Boolean updateUserDetails(User user);

    Boolean insertNewUser(User user);

    List<Profile> getProfiles(String uid);

    Boolean addItemIntoProfile(String uid, String itemId);

    Boolean removeItemFromProfile(String uid, String itemId);

    List<Item> getItemsFromProfile(String uid);

    List<Item> getItemsFromLog(String uid);
}

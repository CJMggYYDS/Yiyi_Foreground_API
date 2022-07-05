package com.yiyi_app.service;

public interface LogService {

    //浏览行为收集
    void addBrowseAction(String uid, String itemId);

    //购买行为收集
    void addBuyAction(String uid, String itemId);

    //收藏行为收集
    void addProfileAction(String uid, String itemId);

    //删除收藏行为
    void removeProfileAction(String uid, String itemId);
}

package com.yiyi_app.service.impl;

import com.yiyi_app.mapper.LogMapper;
import com.yiyi_app.service.LogService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LogServiceImpl implements LogService {

    private static final int BROWSE_ACTION=1;  //浏览行为
    private static final int BUY_ACTION=2;     //购买行为
    private static final int PROFILE_ACTION=3; //收藏行为

    @Resource
    LogMapper logMapper;

    @Override
    @Async("AsyncActionExecutor")
    public void addBrowseAction(String uid, String itemId) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime=new Date();
        String timestamp=format.format(nowTime);
        logMapper.insertUserAction(uid, timestamp, BROWSE_ACTION, itemId);
    }

    @Override
    @Async("AsyncActionExecutor")
    public void addBuyAction(String uid, String itemId) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime=new Date();
        String timestamp=format.format(nowTime);
        logMapper.insertUserAction(uid, timestamp, BUY_ACTION, itemId);
    }

    @Override
    @Async("AsyncActionExecutor")
    public void addProfileAction(String uid, String itemId) {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowTime=new Date();
        String timestamp=format.format(nowTime);
        logMapper.insertUserAction(uid, timestamp, PROFILE_ACTION, itemId);
    }

    @Override
    @Async("AsyncActionExecutor")
    public void removeProfileAction(String uid, String itemId) {
        logMapper.deleteAction(uid, PROFILE_ACTION, itemId);
    }
}

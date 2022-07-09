package com.yiyi_app.recommend.service.impl;

import com.yiyi_app.recommend.dto.UserActiveDTO;
import com.yiyi_app.recommend.mapper.UserActiveMapper;
import com.yiyi_app.recommend.service.UserActiveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserActiveServiceImpl implements UserActiveService {

    @Resource
    UserActiveMapper activeMapper;

    @Override
    public List<UserActiveDTO> listAllUserActive() {
        return activeMapper.listAllUserActiveDTO();
    }

}

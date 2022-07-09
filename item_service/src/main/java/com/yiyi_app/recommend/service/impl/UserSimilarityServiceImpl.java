package com.yiyi_app.recommend.service.impl;

import com.yiyi_app.recommend.dto.UserSimilarityDTO;
import com.yiyi_app.recommend.mapper.UserSimilarityMapper;
import com.yiyi_app.recommend.service.UserSimilarityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserSimilarityServiceImpl implements UserSimilarityService {

    @Resource
    UserSimilarityMapper similarityMapper;

    @Override
    public boolean saveUserSimilarity(UserSimilarityDTO userSimilarityDTO) {
        return similarityMapper.insertUserSimilarity(userSimilarityDTO) > 0;
    }

    @Override
    public boolean updateUserSimilarity(UserSimilarityDTO userSimilarityDTO) {
        return similarityMapper.updateUserSimilarity(userSimilarityDTO) > 0;
    }

    @Override
    public boolean isExistUserSimilarity(UserSimilarityDTO userSimilarityDTO) {
        return similarityMapper.isUserSimilarity(userSimilarityDTO) > 0;
    }

    @Override
    public List<UserSimilarityDTO> getListUserSimilarityByUID(String uid) {
        return similarityMapper.listUserSimilarityById(uid);
    }

    @Override
    public UserSimilarityDTO getUserSimilarityByUID(String uid, String ref_uid) {
        return similarityMapper.getUserSimilarityById(uid, ref_uid);
    }
}

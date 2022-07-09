package com.yiyi_app.recommend.service;


import com.yiyi_app.recommend.dto.UserSimilarityDTO;

import java.util.List;


public interface UserSimilarityService {

    boolean saveUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    boolean updateUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    boolean isExistUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    List<UserSimilarityDTO> getListUserSimilarityByUID(String uid);

    UserSimilarityDTO getUserSimilarityByUID(String uid, String ref_uid);
}

package com.yiyi_app.recommend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  用于表示用户与用户之间的相似度
 * @author cjm
 * @date: 2022/7/6
 */
@Data
@NoArgsConstructor
public class UserSimilarityDTO implements Comparable<UserSimilarityDTO>{
    // 用户uid
    private String uid;

    // 进行比较的用户uid
    private String ref_uid;

    // 两个用户之间的相似度
    private Double similarity;


    @Override
    public int compareTo(UserSimilarityDTO o) {
        return o.getSimilarity().compareTo(this.getSimilarity());
    }
}

package com.yiyi_app.recommend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  用于表示用户行为
 * @author cjm
 * @date: 2022/7/6
 */
@Data
@NoArgsConstructor
public class UserActiveDTO {

    // 该用户uid
    private String uid;

    // 该用户操作的itemId
    private String itemId;

    // 操作该ItemId的次数
    private Long hits;
}

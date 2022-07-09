package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("account")
public class User {
    String uid;
    String username;
    String password;
    String address;
    Integer status;
    String favoritesId;
}

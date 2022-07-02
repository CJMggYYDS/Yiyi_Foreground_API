package com.yiyi_app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    String uid;
    String username;
    String password;
    String address;
    Integer status;
    String favoriteId;
}

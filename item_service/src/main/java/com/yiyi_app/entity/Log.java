package com.yiyi_app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Log {

    String uid;
    String timestamp;
    Integer action;
    String itemId;
}

package com.yiyi_app.entity;


import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;

@Data
@TableName("account")
public class Profile {

    String uid;
    String itemId;
}

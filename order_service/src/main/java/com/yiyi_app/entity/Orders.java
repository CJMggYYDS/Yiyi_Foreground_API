package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("orders")
public class Orders {
    @TableId(type = IdType.INPUT)
    String orderid;

    int orderstatus;

    String uid;

    String address;

    String ordertime;


}

package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("cart")
public class Cart {
    @TableId(value = "uid" ,type = IdType.INPUT)
    String uid;

    @TableId(value = "itemid" ,type = IdType.INPUT)
    String itemId;

    String timestamp;

    BigDecimal price;

    int num;

    int days;
}

package com.yiyi_app.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yiyi_app.entity.Item;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartVO {
    String uid;
    Item item;
    String timestamp;
    BigDecimal price;
    int num;
    int days;
}

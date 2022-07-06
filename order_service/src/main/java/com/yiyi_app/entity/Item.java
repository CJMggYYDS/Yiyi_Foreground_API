package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("item")
public class Item {

    String itemId;
    String classify;
    String itemName;
    BigDecimal price;
    String url;
    Integer inventory;
}

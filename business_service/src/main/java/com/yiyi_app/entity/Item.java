package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("item")
public class Item {

    @TableId(value = "itemid" ,type = IdType.INPUT)
    String itemid;

    String classify;

    String itemName;

    BigDecimal price;

    String url;

    Integer inventory;

}

package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Item {

    @TableId(value = "itemid",type = IdType.INPUT)
    String itemId;
    String classify;
    @TableField(value = "itemname")
    String itemName;
    BigDecimal price;
    String url;
    Integer inventory;
}

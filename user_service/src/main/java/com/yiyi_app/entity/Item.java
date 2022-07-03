package com.yiyi_app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Item {

    String itemId;
    String classify;
    String itemName;
    BigDecimal price;
    String url;
    Integer inventory;
}

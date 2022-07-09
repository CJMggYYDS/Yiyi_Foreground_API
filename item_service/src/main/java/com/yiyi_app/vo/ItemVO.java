package com.yiyi_app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO {

    String itemId;
    String classify;
    String itemName;
    BigDecimal price;
    String url;
    Integer inventory;
    Integer sales;
}

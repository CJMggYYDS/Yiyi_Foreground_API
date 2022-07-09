package com.yiyi_app.vo;

import com.yiyi_app.entity.Item;
import lombok.Data;

@Data
public class RentItemListVO {
  String itemId;
  int days;
  int num;
}

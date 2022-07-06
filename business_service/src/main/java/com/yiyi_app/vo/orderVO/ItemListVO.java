package com.yiyi_app.vo.orderVO;

import com.yiyi_app.entity.Item;
import lombok.Data;

@Data
public class ItemListVO {
  private Item item;
  int status;
  int days;
  int num;
}

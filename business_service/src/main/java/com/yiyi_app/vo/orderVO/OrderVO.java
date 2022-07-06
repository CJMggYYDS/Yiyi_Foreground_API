package com.yiyi_app.vo.orderVO;

import com.yiyi_app.vo.ItemListVO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderVO {
    String orderId;
    int status;
    String uid;
    String address;
    String timestamp;
    List<ItemListVO> itemList = new ArrayList<>();
}

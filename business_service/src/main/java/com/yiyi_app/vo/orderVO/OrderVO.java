package com.yiyi_app.vo.orderVO;

import com.yiyi_app.vo.RentItemListVO;
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
    List<RentItemListVO> itemList = new ArrayList<>();
}

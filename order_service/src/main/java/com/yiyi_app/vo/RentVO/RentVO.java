package com.yiyi_app.vo.RentVO;

import lombok.Data;

import java.util.List;

@Data
public class RentVO {
    private String uid;
    private String orderId;
    private String timestamp;
    private String address;
    int orderstatus;
    int orderliststatus;
    private List<RentItemListVO> itemList;
}

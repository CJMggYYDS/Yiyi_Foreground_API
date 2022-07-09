package com.yiyi_app.vo;

import lombok.Data;

import java.util.List;

@Data
public class RentVO {
    private String timestamp;
    private String address;
    private List<ItemListVO> itemList;
}

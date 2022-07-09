package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("orderlist")
public class Orderlist {
    @TableId(type = IdType.INPUT)
    String orderid;
    String itemId;
    int orderListstatus;
    int days;
    int num;
}

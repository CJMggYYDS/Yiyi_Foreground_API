package com.yiyi_app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("orderlist")
public class Orderlist {

    String orderid;

    String itemid;

    int orderliststatus;

    int days;

    int num;

    BigDecimal unitprice;
}

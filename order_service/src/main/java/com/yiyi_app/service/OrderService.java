package com.yiyi_app.service;

import com.yiyi_app.entity.Orderlist;
import com.yiyi_app.entity.Orders;
import com.yiyi_app.vo.OrderVO;

import java.util.List;

public interface OrderService {

    Boolean insertOrder(Orders order,Orderlist orderlist);

    Boolean updateOrderStatus(String uid,String itemId,int status);

    Boolean updateOrder(String uid,String orderId,String address,String itemId,int days,int num);

    Boolean deleteOrder(String uid,String orderId);

    OrderVO getOrderByOrderId(String orderId);

    List<OrderVO> getOrderByuid(String uid);

}

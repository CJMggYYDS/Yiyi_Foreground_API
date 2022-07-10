package com.yiyi_app.service;

import com.yiyi_app.entity.Orders;
import com.yiyi_app.entity.Orderlist;
import com.yiyi_app.vo.OrderVO;

import java.util.List;

public interface OrderService {

    Boolean insertOrder(Orders orders);

    Boolean insertOrderList(Orderlist orderlist);

    Boolean updateOrderStatus(String orderId,String itemId,int orderListStatus);

    Boolean updateOrder(String uid,String orderId,String address,String itemId,int days,int num);

    Boolean deleteOrder(String uid,String orderId);

    OrderVO getOrderByOrderId(String orderId);

    List<OrderVO> getOrderByuid(String uid);

}

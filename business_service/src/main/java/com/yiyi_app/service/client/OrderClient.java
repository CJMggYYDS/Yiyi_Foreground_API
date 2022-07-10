package com.yiyi_app.service.client;

import com.yiyi_app.entity.Orderlist;
import com.yiyi_app.entity.Orders;
import com.yiyi_app.vo.RentVO;
import com.yiyi_app.vo.orderVO.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @param: *
* @description: 使用OpenFeign调用order-service微服务
* @author: egg
* @create: 2022/7/4
*/
@Component
@FeignClient(value ="order-service")
public interface OrderClient {

    @PostMapping(value = "/orders/insertOrder")
    boolean insertOrder(@RequestBody RentVO rentVO);

    @PostMapping("/orders/updateOrderStatus")
    boolean updateOrderStatus(@RequestParam("orderId") String orderId,@RequestParam("itemId") String itemId,@RequestParam("orderListStatus") int orderListStatus);

    @PostMapping("/users/order/getOrderByUid")
    List<List<OrderVO>> getOrderByUid(@RequestBody String uid);

    @PostMapping("/users/order/getOrderListByItemId")
    Orderlist getOrderListById(@RequestParam("orderId") String orderId,@RequestParam("itemId") String itemId);

    @PostMapping("/users/order/getOrdersByOrderId")
    Orders getOrdersByOrderId(@RequestParam("orderId") String orderId);

    @PostMapping("/users/order/getOrderListByOrderId")
    List<Orderlist> getOrderListByOrderId(@RequestParam("orderId") String orderId);
}

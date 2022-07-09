package com.yiyi_app.service.client;

import com.yiyi_app.entity.Orderlist;
import com.yiyi_app.entity.Orders;
import com.yiyi_app.vo.orderVO.OrderVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
* @param: *
* @description: 使用OpenFeign调用order-service微服务
* @author: egg
* @create: 2022/7/4
*/
@FeignClient("order-service")
public interface OrderClient {

    @PostMapping("/orders")
    boolean insertOrder(@RequestBody Map<String, Object> map );

    @PutMapping("/orders")
    boolean updateOrderStatus(@RequestParam("uid") String uid,@RequestParam("itemId") String itemId,@RequestParam("status") int status);

    @GetMapping("/users/order")
    List<OrderVO> getOrderByUid(@RequestBody String uid);

    @GetMapping("/users/order")
    Orderlist getOrderListByItemId(@RequestBody String itemId);

    @GetMapping("/users/order")
    Orders getOrdersByItemId(@RequestBody String uid);
}

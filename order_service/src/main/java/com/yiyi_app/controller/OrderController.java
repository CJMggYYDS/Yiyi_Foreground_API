package com.yiyi_app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiyi_app.entity.Orderlist;
import com.yiyi_app.entity.Orders;
import com.yiyi_app.mapper.OrderlistMapper;
import com.yiyi_app.mapper.OrdersMapper;
import com.yiyi_app.service.OrderService;
import com.yiyi_app.util.ResponseResult;
import com.yiyi_app.vo.OrderVO;
import com.yiyi_app.vo.RentVO.RentVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Resource
    OrderService orderService;
    @Resource
    OrderlistMapper orderlistMapper;
    @Resource
    OrdersMapper ordersMapper;

    public static int count;
    public static boolean result;

    /**
    * @param: order,orderlist
    * @description: 新增订单
    * @author: egg
    * @create: 2022/7/4
    */
    @PostMapping("/order")
    public ResponseResult insertOrder(@RequestHeader("uid") String uid,@RequestBody Map<String, Object> map ){
        Orderlist orderlist = new Orderlist();
        orderlist.setOrderid(map.get("orderid").toString());
        orderlist.setOrderliststatus(Integer.parseInt(map.get("status").toString()));
        orderlist.setItemid(map.get("itemid").toString());
        orderlist.setDays(Integer.parseInt(map.get("days").toString()));
        orderlist.setNum(Integer.parseInt(map.get("num").toString()));
        
        Orders orders = new Orders();
        orders.setOrderid(map.get("orderid").toString());
        orders.setOrderstatus(Integer.parseInt(map.get("status").toString()));
        orders.setUid(uid);
        orders.setAddress(map.get("address").toString());
        orders.setOrdertime(map.get("ordertime").toString());

        boolean res2 = orderService.insertOrderList(orderlist);
        boolean res = orderService.insertOrder(orders);

        if(res & res2) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
     * @param: order,orderlist
     * @description: 插入订单远程调用接口
     * @author: egg
     * @create: 2022/7/4
     */
    @PostMapping("/orders/insertOrder")
    public boolean insertOrder(@RequestBody RentVO rentVO){
        System.out.println(rentVO);
        System.out.println(rentVO.getItemList());
        System.out.println(rentVO.getItemList().size());

        Orders orders = new Orders();
        orders.setOrderid(rentVO.getOrderId());
        orders.setOrderstatus(rentVO.getOrderstatus());
        orders.setUid(rentVO.getUid());
        orders.setAddress(rentVO.getAddress());
        orders.setOrdertime(rentVO.getTimestamp());
        System.out.println(orders);
        boolean res2 = orderService.insertOrder(orders);

        for(int i = 0 ; i<rentVO.getItemList().size();i++){
            System.out.println(rentVO);
            Orderlist orderlist = new Orderlist();
            orderlist.setOrderid(rentVO.getOrderId());
            orderlist.setOrderliststatus(rentVO.getOrderliststatus());
            orderlist.setItemid(rentVO.getItemList().get(i).getItemId());
            orderlist.setDays(rentVO.getItemList().get(i).getDays());
            orderlist.setNum(rentVO.getItemList().get(i).getNum());
            orderlist.setUnitprice(BigDecimal.valueOf(99));
            System.out.println(orderlist);

            boolean res = orderService.insertOrderList(orderlist);
            System.out.println("res"+res);
            System.out.println(rentVO);
            if (res){
                count++;
                System.out.println("count"+count);
            }

        }


        if(count == rentVO.getItemList().size() & res2)
            result=true;
        else
            result = false;

        return  result;
    }

    /**
    * @param: status
    * @description: 更新订单状态远程调用接口
    * @author: egg
    * @create: 2022/7/4
    */
    @PostMapping("/orders/updateOrderStatus")
    public Boolean updateOrderStatus(@RequestParam String orderId,@RequestParam String itemId,@RequestParam int orderListStatus){
        boolean res = orderService.updateOrderStatus(orderId,itemId,orderListStatus);
        return res;
    }

    /**
    * @param: uid,orderId, itemId,address,num ,days
    * @description: 支付前可修改订单信息
    * @author: egg
    * @create: 2022/7/4
    */
    @PutMapping("/order/{orderId}")
    public ResponseResult updateOrder(@RequestHeader("uid") String uid,@PathVariable("orderId") String orderId,@RequestBody String itemId,@RequestBody String address, @RequestBody int num, @RequestBody int days){
        Boolean res = orderService.updateOrder(uid,orderId, itemId,address,num ,days);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
     * @param: uid,orderId
     * @description: 取消订单
     * @author: egg
     * @create: 2022/7/4
     */
    @DeleteMapping("/order/{orderId}")
    public ResponseResult deleteOrder(@RequestHeader("uid") String uid,@PathVariable("orderId") String orderId){
        Boolean res = orderService.deleteOrder(uid,orderId);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
    * @param: uid
    * @description: 查看用户所有订单
    * @author: egg
    * @create: 2022/7/4
    */
    @GetMapping("/order")
    @ResponseBody
    public ResponseResult getOrder(@RequestHeader("uid") String uid){
        List<OrderVO> responseData = orderService.getOrderByuid(uid);
        if(responseData == null) {
            return ResponseResult.error();
        }
        else {
            return ResponseResult.success(responseData);
        }
    }

    /**
     * @param: uid
     * @description: 查看用户所有订单远程调用接口
     * @author: egg
     * @create: 2022/7/4
     */
    @PostMapping("/users/order/getOrderByUid")
    public List<OrderVO> getOrderByUid(@RequestHeader("uid") String uid){
        List<OrderVO> responseData = orderService.getOrderByuid(uid);
        return responseData;
    }

    /**
     * @param: uid
     * @description: 获取itemid对应订单远程调用接口
     * @author: egg
     * @create: 2022/7/4
     */
    @PostMapping("/users/order/getOrderListByItemId")
    public Orderlist getOrderListById(@RequestParam("orderId") String orderId,@RequestParam("itemId") String itemId){
        System.out.println("itemId"+itemId+",orderId"+orderId);
        QueryWrapper<Orderlist> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("itemid",itemId);
        QueryWrapper.eq("orderId",orderId);
        Orderlist responseData = orderlistMapper.selectOne(QueryWrapper);
        System.out.println("getOrderListByItemId"+responseData);
        return responseData;
    }

    /**
     * @param: uid
     * @description: 获取itemid对应订单详情远程调用接口
     * @author: egg
     * @create: 2022/7/4
     */
    @PostMapping("/users/order/getOrdersByOrderId")
    public Orders getOrdersByOrderId(@RequestParam("orderId") String orderId){
        System.out.println("orderId"+orderId);
        QueryWrapper<Orders> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("orderId",orderId);
        Orders responseData = ordersMapper.selectOne(QueryWrapper);
        System.out.println("getOrderListByItemId"+responseData);
        return responseData;
    }

    @PostMapping("/users/order/getOrderListByOrderId")
    public List<Orderlist> getOrderListByOrderId(@RequestParam("orderId") String orderId){
        System.out.println("orderId"+orderId);
        QueryWrapper<Orderlist> QueryWrapper = new QueryWrapper<>();
        QueryWrapper.eq("orderId",orderId);
        List<Orderlist> responseData = orderlistMapper.selectList(QueryWrapper);
        System.out.println("getOrderListByOrderId"+responseData);
        return responseData;
    }
}

package com.yiyi_app.controller;

import com.yiyi_app.entity.Item;
import com.yiyi_app.entity.Orderlist;
import com.yiyi_app.entity.Orders;
import com.yiyi_app.mapper.OrderlistMapper;
import com.yiyi_app.mapper.OrdersMapper;
import com.yiyi_app.service.OrderService;
import com.yiyi_app.util.ResponseResult;
import com.yiyi_app.vo.OrderVO;
import com.yiyi_app.vo.RentItemListVO;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.interfaces.PBEKey;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    @PostMapping("/orders")
    public ResponseResult insertOrder(@RequestHeader("uid") String uid,@RequestBody Map<String, Object> map ){
        Orderlist orderlist = new Orderlist();
        orderlist.setOrderid(map.get("orderid").toString());
        orderlist.setOrderListstatus(Integer.parseInt(map.get("status").toString()));
        orderlist.setItemId(map.get("itemid").toString());
        orderlist.setDays(Integer.parseInt(map.get("days").toString()));
        orderlist.setNum(Integer.parseInt(map.get("num").toString()));
        
        Orders order = new Orders();
        order.setOrderid(map.get("orderid").toString());
        order.setOrderstatus(Integer.parseInt(map.get("status").toString()));
        order.setUid(uid);
        order.setAddress(map.get("address").toString());
        order.setOrdertime(map.get("ordertime").toString());
        
        boolean res = orderService.insertOrder(order,orderlist);
        if(res) {
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
    @PostMapping("/orders")
    public Boolean insertOrder(@RequestBody Map<String, Object> map ){

        ArrayList<Object> list = (ArrayList<Object>) map.get("itemList");
        ArrayList<RentItemListVO> rentItemListVOS = new ArrayList<>();
        RentItemListVO itemList = null;
        for (Object object : list) {
            itemList = new RentItemListVO();
            itemList.setItemId(((LinkedHashMap<Object, Object>) object).get("itemId").toString());
            itemList.setDays(Integer.parseInt(((LinkedHashMap<Object, Object>) object).get("days").toString()));
            itemList.setNum(Integer.parseInt(((LinkedHashMap<Object, Object>) object).get("num").toString()));
            rentItemListVOS.add(itemList);
        }

        for(int i = 0 ; i<rentItemListVOS.size();i++){
            Orderlist orderlist = new Orderlist();
            orderlist.setOrderid(map.get("orderid").toString());
            orderlist.setOrderListstatus(Integer.parseInt(map.get("status").toString()));
            orderlist.setItemId(rentItemListVOS.get(i).getItemId());
            orderlist.setDays(rentItemListVOS.get(i).getDays());
            orderlist.setNum(rentItemListVOS.get(i).getNum());

            Orders order = new Orders();
            order.setOrderid(map.get("orderid").toString());
            order.setOrderstatus(Integer.parseInt(map.get("status").toString()));
            order.setUid(map.get("uid").toString());
            order.setAddress(map.get("address").toString());
            order.setOrdertime(map.get("ordertime").toString());

            boolean res = orderService.insertOrder(order,orderlist);

            if (res)
                count++;
        }

        if(count == rentItemListVOS.size())
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
    @PutMapping("/orders/")
    public Boolean updateOrderStatus(@RequestParam String uid,@RequestParam String itemId,@RequestParam int status){
        boolean res = orderService.updateOrderStatus(uid,itemId,status);
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
    @PutMapping("/order/{orderId}")
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
    @GetMapping("/users/order")
    @ResponseBody
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
    @GetMapping("/users/order")
    @ResponseBody
    public Orderlist getOrderListByItemId(@RequestBody String itemId){
        Orderlist responseData = orderlistMapper.selectById(itemId);
        return responseData;
    }

    /**
     * @param: uid
     * @description: 获取itemid对应订单详情远程调用接口
     * @author: egg
     * @create: 2022/7/4
     */
    @GetMapping("/users/order")
    @ResponseBody
    public Orders getOrderByItemId(@RequestBody String uid){
        Orders responseData = ordersMapper.selectById(uid);
        return responseData;
    }
}

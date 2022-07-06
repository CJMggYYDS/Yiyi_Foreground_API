package com.yiyi_app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiyi_app.entity.Item;
import com.yiyi_app.entity.Orderlist;
import com.yiyi_app.entity.Orders;
import com.yiyi_app.mapper.OrderlistMapper;
import com.yiyi_app.mapper.OrdersMapper;
import com.yiyi_app.service.OrderService;
import com.yiyi_app.service.client.ItemClient;
import com.yiyi_app.vo.ItemListVO;
import com.yiyi_app.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    //自动注入Mapper
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderlistMapper orderlistMapper;

    @Resource
    ItemClient itemClient;

    public static String orderId;

    @Override
    public Boolean insertOrder(Orders order, Orderlist orderlist) {
        return (ordersMapper.insert(order)& orderlistMapper.insert(orderlist) )> 0;
    }

    @Override
    public Boolean updateOrderStatus(String uid, String itemId,int status) {
        Orderlist orderlist1 = orderlistMapper.selectById(itemId) ;

        Orderlist orderlist = new Orderlist();
        orderlist.setOrderid(orderlist1.getOrderid());
        orderlist.setItemId(itemId);
        orderlist.setOrderListstatus(status);
        UpdateWrapper<Orderlist> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("orderid", orderlist1.getOrderid());
        updateWrapper.eq("itemId", itemId);

        Orders orders = new Orders();
        orders.setUid(uid);
        orders.setOrderid(orderlist1.getOrderid());
        orders.setOrderstatus(status);
        UpdateWrapper<Orders> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("uid", uid);
        updateWrapper2.eq("orderid", orderlist1.getOrderid());

        return ( ordersMapper.update(orders,updateWrapper2) & orderlistMapper.update(orderlist,updateWrapper) ) != 0;

    }

    @Override
    public Boolean updateOrder(String uid,String orderId,String address,String itemId,int days,int num) {
        Orderlist orderlist = new Orderlist();
        orderlist.setOrderid(orderId);
        orderlist.setItemId(itemId);
        orderlist.setDays(days);
        orderlist.setNum(num);
        UpdateWrapper<Orderlist> OiderListUpdateWrapper = new UpdateWrapper<>();
        OiderListUpdateWrapper.eq("orderid",orderId);
        OiderListUpdateWrapper.eq("itemId",itemId);

        Orders orders = new Orders();
        orders.setOrderid(orderId);
        orders.setUid(uid);
        orders.setAddress(address);
        UpdateWrapper<Orders> ordersUpdateWrapper = new UpdateWrapper<>();
        ordersUpdateWrapper.eq("uid",uid);
        ordersUpdateWrapper.eq("orderid",orderId);

        return ( orderlistMapper.update(orderlist,OiderListUpdateWrapper) & ordersMapper.update(orders,ordersUpdateWrapper)) !=0;
    }

    @Override
    public Boolean deleteOrder(String uid, String orderId) {
        QueryWrapper<Orderlist> OrderlistQueryWrapper = new QueryWrapper<>();
        OrderlistQueryWrapper.eq("orderid",orderId);
        Orderlist orderlist = orderlistMapper.selectOne(OrderlistQueryWrapper);

        QueryWrapper<Orders> OrdersQueryWrapper = new QueryWrapper<>();
        OrdersQueryWrapper.eq("uid",uid);
        OrdersQueryWrapper.eq("orderid",orderId);
        Orders orders = ordersMapper.selectOne(OrdersQueryWrapper);

        if (orderlist == null || orders ==null)
            return false;
        else
            return ( orderlistMapper.delete(OrderlistQueryWrapper) & ordersMapper.delete(OrdersQueryWrapper)) !=0;
    }

    @Override
    public OrderVO getOrderByOrderId(String orderId) {
        Orders orders = ordersMapper.selectById(orderId);

        QueryWrapper<Orderlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderid",orderId);
        Orderlist orderlist = orderlistMapper.selectOne(queryWrapper);

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderlist.getOrderid());
        orderVO.setStatus(orderlist.getOrderListstatus());
        orderVO.setAddress(orders.getAddress());
        orderVO.setUid(orders.getUid());
        orderVO.setTimestamp(orders.getOrdertime());

        ItemListVO itemListVO = new ItemListVO();
        Item item=itemClient.getItemByItemId(orderlist.getItemId());
        itemListVO.setItem(item);
        itemListVO.setDays(orderlist.getDays());
        itemListVO.setStatus(orderlist.getOrderListstatus());
        itemListVO.setNum(orderlist.getNum());
        List<ItemListVO> itemList = new ArrayList<>();
        itemList.add(itemListVO);
        orderVO.setItemList(itemList);
        return orderVO;
    }


    @Override
    public List<OrderVO> getOrderByuid(String uid) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        List<Orders> orders = ordersMapper.selectList(queryWrapper);
        List<OrderVO> orderVOList = new ArrayList<>();
        for (Orders order : orders) {
            orderVOList.add(getOrderByOrderId(order.getOrderid()));
        }
        return orderVOList;
    }

}

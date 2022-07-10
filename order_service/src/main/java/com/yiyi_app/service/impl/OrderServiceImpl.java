package com.yiyi_app.service.impl;

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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrdersMapper ordersMapper;

    @Resource
    private OrderlistMapper orderlistMapper;

    @Resource
    ItemClient itemClient;

    public static String orderId;

    @Override
    public Boolean insertOrder(Orders orders) {
        return ordersMapper.insert(orders)> 0;
    }

    @Override
    public Boolean insertOrderList(Orderlist orderlist) {
        return orderlistMapper.insert(orderlist) > 0;
    }

    @Override
    public Boolean updateOrderStatus(String orderId, String itemId,int orderListStatus) {
        Orderlist orderlist = new Orderlist();
        orderlist.setOrderid(orderId);
        orderlist.setItemid(itemId);
        orderlist.setOrderliststatus(orderListStatus);
        UpdateWrapper<Orderlist> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("orderid", orderId);
        updateWrapper.eq("itemId", itemId);

        QueryWrapper<Orders> OrdersQueryWrapper = new QueryWrapper<>();
        OrdersQueryWrapper.eq("orderid",orderId);
        Orders orders1 = ordersMapper.selectOne(OrdersQueryWrapper);

        Orders orders = new Orders();
        orders.setOrderid(orderId);
        orders.setOrderstatus(orders1.getOrderstatus()-1);
        UpdateWrapper<Orders> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper2.eq("orderid", orderId);

        return ( ordersMapper.update(orders,updateWrapper2) & orderlistMapper.update(orderlist,updateWrapper) ) != 0;

    }

    @Override
    public Boolean updateOrder(String uid,String orderId,String address,String itemId,int days,int num) {
        Orderlist orderlist = new Orderlist();
        orderlist.setOrderid(orderId);
        orderlist.setItemid(itemId);
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
    public List<OrderVO> getOrderByOrderId(String orderId) {
        Orders orders = ordersMapper.selectById(orderId);

        QueryWrapper<Orderlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderid",orderId);
        List<Orderlist> orderlist = orderlistMapper.selectList(queryWrapper);
        List<OrderVO> orderVOList = new ArrayList<>();

        for(int i=0;i<orderlist.size();i++){
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderId(orderlist.get(i).getOrderid());
            orderVO.setStatus(orderlist.get(i).getOrderliststatus());
            orderVO.setAddress(orders.getAddress());
            orderVO.setUid(orders.getUid());
            orderVO.setTimestamp(orders.getOrdertime());

            ItemListVO itemListVO = new ItemListVO();
            Item item=itemClient.getItemsByItemId(orderlist.get(i).getItemid());
            itemListVO.setItem(item);
            itemListVO.setDays(orderlist.get(i).getDays());
            itemListVO.setStatus(orderlist.get(i).getOrderliststatus());
            itemListVO.setNum(orderlist.get(i).getNum());
            List<ItemListVO> itemList = new ArrayList<>();
            itemList.add(itemListVO);
            orderVO.setItemList(itemList);
            orderVOList.add(orderVO);
        }

        return orderVOList;
    }


    @Override
    public List<List<OrderVO>> getOrderByuid(String uid) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        List<Orders> orders = ordersMapper.selectList(queryWrapper);
        List<List<OrderVO>> orderVOList = new ArrayList<>();

        for (Orders order : orders) {
            orderVOList.add(getOrderByOrderId(order.getOrderid()));
        }
        return orderVOList;
    }

}

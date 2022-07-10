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
import net.sf.jsqlparser.expression.OrderByClause;
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

    public static int count;

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
        List<Orderlist> orderlist = orderlistMapper.selectList(OrderlistQueryWrapper);

        QueryWrapper<Orders> OrdersQueryWrapper = new QueryWrapper<>();
        OrdersQueryWrapper.eq("uid",uid);
        OrdersQueryWrapper.eq("orderid",orderId);
        Orders orders = ordersMapper.selectOne(OrdersQueryWrapper);
        int deleteorderlistRes = orderlistMapper.delete(OrderlistQueryWrapper);
        int deleteordersRes = ordersMapper.delete(OrdersQueryWrapper);
        System.out.println("deleteorderlistRes:"+deleteorderlistRes+", deleteordersRes"+deleteordersRes);
        boolean res = false;
        if(deleteorderlistRes == orderlist.size() & deleteordersRes ==1)
            res = true;
        System.out.println(res);
        if (orderlist == null || orders ==null)
            return false;
        else
            return res;
    }

    @Override
    public OrderVO getOrderByOrderId(String orderId) {
        Orders orders = ordersMapper.selectById(orderId);

        QueryWrapper<Orderlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orderid",orderId);
        List<Orderlist> orderlist = orderlistMapper.selectList(queryWrapper);
        //List<OrderVO> orderVOList = new ArrayList<>();
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setStatus(orderlist.get(0).getOrderliststatus());
        orderVO.setAddress(orders.getAddress());
        orderVO.setUid(orders.getUid());
        orderVO.setTimestamp(orders.getOrdertime());

        List<ItemListVO> itemList = new ArrayList<>();
        for(int i=0;i<orderlist.size();i++){
            ItemListVO itemListVO = new ItemListVO();
            Item item=itemClient.getItemsByItemId(orderlist.get(i).getItemid());
            itemListVO.setItem(item);
            itemListVO.setDays(orderlist.get(i).getDays());
            itemListVO.setStatus(orderlist.get(i).getOrderliststatus());
            itemListVO.setNum(orderlist.get(i).getNum());
            itemList.add(itemListVO);
            orderVO.setItemList(itemList);
            //orderVOList.add(orderVO);
        }

        return orderVO;
    }


    @Override
    public List<OrderVO> getOrderByuid(String uid) {
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid);
        List<Orders> orders = ordersMapper.selectList(queryWrapper);
        System.out.println("orders"+orders);
        List<OrderVO> orderVOList = new ArrayList<>();

        for (Orders order : orders) {
            orderVOList.add(getOrderByOrderId(order.getOrderid()));
        }
        return orderVOList;
    }

}

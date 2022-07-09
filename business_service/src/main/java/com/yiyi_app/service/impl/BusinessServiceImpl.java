package com.yiyi_app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiyi_app.entity.Cart;
import com.yiyi_app.persistence.CartMapper;
import com.yiyi_app.service.BusinessService;
import com.yiyi_app.service.client.ItemClient;
import com.yiyi_app.service.client.OrderClient;
import com.yiyi_app.vo.RentVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BusinessServiceImpl implements BusinessService {

    @Resource
    CartMapper cartMapper;

    @Resource
    ItemClient itemClient;

    @Resource
    OrderClient orderClient;

    //租赁商品下单
    @Override
    public Boolean rentItem(String uid, RentVO rentVO) {
        String orderID = UUID.randomUUID().toString().trim().replaceAll("-", "");

        Map<String,Object> map = new HashMap<String,Object>();//创建Map对象，Object是所有类型的父类
        map.put("uid",uid);
        map.put("orderid",orderID);
        map.put("itemList",rentVO.getItemList());
        map.put("status",1);
        map.put("address",rentVO.getAddress());
        map.put("ordertime",rentVO.getTimestamp());
        Boolean res = orderClient.insertOrder(map);

        return res;
    }

    @Override
    public Boolean returnItem(String uid, String itemid) {
        boolean res = orderClient.updateOrderStatus(uid,itemid,0);
        return res;
    }

    @Override
    public Boolean addCart(String uid, String itemId, int num, int days) {
        Cart cart = new Cart();
        cart.setUid(uid);
        cart.setItemId(itemId);
        cart.setPrice(itemClient.getItemsByItemId(itemId).getPrice());

        //获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateToday = date.format(formatter).trim().replaceAll("-", "");
        cart.setTimestamp(dateToday);

        cart.setNum(num);
        cart.setDays(days);

        return cartMapper.insert(cart) > 0 ;
    }

    @Override
    public Boolean updateCart(String uid, String itemId, int num, int days) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("uid",uid);
        cartQueryWrapper.eq("itemId",itemId);
        Cart cart1 = cartMapper.selectOne(cartQueryWrapper);

        Cart cart = new Cart();
        cart.setUid(uid);
        cart.setItemId(itemId);
        cart.setNum(num);
        cart.setDays(days);

        UpdateWrapper<Cart> cartUpdateWrapper = new UpdateWrapper<>();
        cartUpdateWrapper.eq("uid",uid);
        cartUpdateWrapper.eq("itemId",itemId);

        return cartMapper.update(cart,cartUpdateWrapper) > 0;
    }

    @Override
    public List<Cart> getCartByUid(String Uid) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("uid",Uid);
        List<Cart> cartList = cartMapper.selectList(cartQueryWrapper);
        return cartList;
    }

    @Override
    public Boolean removeItemFromCartByItemId(String uid, String itemId) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("uid",uid);
        cartQueryWrapper.eq("itemId",itemId);
        Cart cart = cartMapper.selectOne(cartQueryWrapper);
        if (cart == null)
            return false;
        else
            return cartMapper.delete(cartQueryWrapper) > 0;
    }

    @Override
    public Boolean payForOrder(String uid, String orderId) {
        return null;
    }
}

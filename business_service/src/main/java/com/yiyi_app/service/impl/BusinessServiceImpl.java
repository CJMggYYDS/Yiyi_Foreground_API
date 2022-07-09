package com.yiyi_app.service.impl;

import com.alipay.api.domain.OrderStatusData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yiyi_app.entity.Cart;
import com.yiyi_app.persistence.CartMapper;
import com.yiyi_app.service.BusinessService;
import com.yiyi_app.service.client.ItemClient;
import com.yiyi_app.service.client.OrderClient;
import com.yiyi_app.vo.CartVO;
import com.yiyi_app.vo.RentVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    public Boolean rentItem(RentVO rentVO) {
        System.out.println(rentVO);
        System.out.println(rentVO.getItemList().get(0).getItemId());
        System.out.println(rentVO.getItemList().get(1).getItemId());
        Boolean res = orderClient.insertOrder(rentVO);
        System.out.println("#############");
        System.out.println("#############rentVO"+rentVO);
        return res;
    }

    @Override
    public Boolean returnItem(String orderId, String itemid) {
        boolean res = orderClient.updateOrderStatus(orderId,itemid,0);
        return res;
    }

    @Override
    public Boolean addCart(String uid, String itemId, int num, int days) {
        Cart cart = new Cart();
        cart.setUid(uid);
        cart.setItemid(itemId);
        cart.setPrice(itemClient.getItemsByItemId(itemId).getPrice());

        //获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateToday = date.format(formatter).trim().replaceAll("-", "");
        cart.setTimestamp(dateToday);

        cart.setNum(num);
        cart.setDays(days);
        System.out.println("insert cart:"+cart);

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
        cart.setItemid(itemId);
        cart.setNum(num);
        cart.setDays(days);

        UpdateWrapper<Cart> cartUpdateWrapper = new UpdateWrapper<>();
        cartUpdateWrapper.eq("uid",uid);
        cartUpdateWrapper.eq("itemId",itemId);

        return cartMapper.update(cart,cartUpdateWrapper) > 0;
    }

    @Override
    public List<CartVO> getCartByUid(String Uid) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("uid",Uid);
        List<Cart> cartList = cartMapper.selectList(cartQueryWrapper);
        List<CartVO> cartVOList = new ArrayList<>();
        CartVO cartVO = new CartVO();
        for (Cart cart : cartList) {
            cartVO.setItem(itemClient.getItemsByItemId(cart.getItemid()));
            cartVO.setUid(Uid);
            cartVO.setDays(cart.getDays());
            cartVO.setNum(cart.getNum());
            cartVO.setPrice(cart.getPrice());
            cartVO.setTimestamp(cart.getTimestamp());
            cartVOList.add(cartVO);
        }
        return cartVOList;
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

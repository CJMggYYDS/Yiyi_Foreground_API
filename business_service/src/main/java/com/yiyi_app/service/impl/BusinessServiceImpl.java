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
        System.out.println("uid:"+uid);
        cart.setItemid(itemId);
        System.out.println("itemId:"+itemId);
        System.out.println("item:"+itemClient.getItemsByItemId(itemId));
        cart.setPrice(itemClient.getItemsByItemId(itemId).getPrice());

        System.out.println("Price:"+itemClient.getItemsByItemId(itemId).getPrice());
        //获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateToday = date.format(formatter).trim().replaceAll("-", "");
        System.out.println("dateToday"+dateToday);
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
        System.out.println("uid:"+uid);
        cartQueryWrapper.eq("itemId",itemId);
        System.out.println("itemId:"+itemId);
        Cart cart1 = cartMapper.selectOne(cartQueryWrapper);

        Cart cart = new Cart();
        cart1.setUid(uid);
        cart1.setItemid(itemId);
        cart1.setNum(num);
        cart1.setDays(days);
        System.out.println("uid:"+uid+"itemId:"+itemId+"num:"+num+"days:"+days);
        UpdateWrapper<Cart> cartUpdateWrapper = new UpdateWrapper<>();
        cartUpdateWrapper.eq("uid",uid);
        cartUpdateWrapper.eq("itemId",itemId);

        return cartMapper.update(cart1,cartUpdateWrapper) > 0;
    }

    @Override
    public List<CartVO> getCartByUid(String Uid) {
        QueryWrapper<Cart> cartQueryWrapper = new QueryWrapper<>();
        cartQueryWrapper.eq("uid",Uid);
        List<Cart> cartList = cartMapper.selectList(cartQueryWrapper);
        System.out.println("cartList"+cartList);
        List<CartVO> cartVOList = new ArrayList<>();

//        for (Cart cart : cartList) {
//            cartVO.setItem(itemClient.getItemsByItemId(cart.getItemid()));
//            cartVO.setUid(Uid);
//            cartVO.setDays(cart.getDays());
//            cartVO.setNum(cart.getNum());
//            cartVO.setPrice(cart.getPrice());
//            cartVO.setTimestamp(cart.getTimestamp());
//            cartVOList.add(cartVO);
//        }
        for (int i = 0 ; i <cartList.size();i++){
            CartVO cartVO = new CartVO();
            cartVO.setItem(itemClient.getItemsByItemId(cartList.get(i).getItemid()));
            System.out.println("Itemid"+i+","+cartList.get(i).getItemid());
            System.out.println("Item"+i+","+itemClient.getItemsByItemId(cartList.get(i).getItemid()));
            cartVO.setUid(Uid);
            cartVO.setDays(cartList.get(i).getDays());
            cartVO.setNum(cartList.get(i).getNum());
            cartVO.setPrice(cartList.get(i).getPrice());
            cartVO.setTimestamp(cartList.get(i).getTimestamp());
            cartVOList.add(cartVO);
            System.out.println("cartVOList "+ i +","+cartVOList);
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

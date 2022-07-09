package com.yiyi_app.service;


import com.yiyi_app.entity.Cart;
import com.yiyi_app.vo.CartVO;
import com.yiyi_app.vo.RentVO;

import java.math.BigDecimal;
import java.util.List;


public interface BusinessService {

    Boolean rentItem(RentVO rentVO);

    Boolean returnItem(String orderId, String itemid);

    Boolean addCart(String uid, String itemId,int num, int days);

    Boolean updateCart(String uid,String itemId,int num,int days);

    List<CartVO> getCartByUid(String Uid);

    Boolean removeItemFromCartByItemId(String uid, String itemId);

    Boolean payForOrder(String uid,String orderId);
}

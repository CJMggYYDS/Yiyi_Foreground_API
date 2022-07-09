package com.yiyi_app.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.yiyi_app.dto.AliPay;
import com.yiyi_app.entity.*;

import com.yiyi_app.service.BusinessService;
import com.yiyi_app.service.client.OrderClient;
import com.yiyi_app.util.ResponseCodeEnum;
import com.yiyi_app.util.ResponseResult;
import com.yiyi_app.vo.ItemListVO;
import com.yiyi_app.vo.RentVO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class businessController {
    @Resource
    BusinessService businessService;
    @Resource
    OrderClient orderClient;
    public static boolean PayRes;


    /**
    * @param:
    * @description: 租赁商品直接下单
    * @author: egg
    * @create: 2022/7/4
    */
    @PostMapping("/business/order")
    public ResponseResult rentItem(@RequestHeader("uid") String uid,@RequestBody Map<String, Object> map){
        ArrayList<Object> list = (ArrayList<Object>) map.get("itemList");
        ArrayList<ItemListVO> itemListVO = new ArrayList<>();
        ItemListVO itemList = null;
        for (Object object : list) {
            itemList = new ItemListVO();
            itemList.setItemId(((LinkedHashMap<Object, Object>) object).get("itemId").toString());
            itemList.setDays(Integer.parseInt(((LinkedHashMap<Object, Object>) object).get("days").toString()));
            itemList.setNum(Integer.parseInt(((LinkedHashMap<Object, Object>) object).get("num").toString()));
            itemListVO.add(itemList);
        }

        RentVO rentVO = new RentVO();
        rentVO.setTimestamp(map.get("timestamp").toString());
        rentVO.setAddress(map.get("address").toString());
        rentVO.setItemList(itemListVO);

        Boolean res = businessService.rentItem(uid,rentVO);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
    * @param:
    * @description: 归还商品
    * @author: egg
    * @create: 2022/7/4
    */
    @PostMapping("/business/order/return")
    public ResponseResult returnItem(@RequestHeader("uid") String uid,@RequestBody String itemId){
        
        //获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateToday = date.format(formatter).trim().replaceAll("-", "");
        //获取租赁购买的天数
        Orderlist orderlist = orderClient.getOrderListByItemId(itemId);
        int dayRent = orderlist.getDays();
        //获取下单日期
        Orders orders = orderClient.getOrdersByItemId(orderlist.getOrderid());
        String dateRent = orders.getOrdertime().trim().replaceAll("-", "");
        //计算租赁服装天数，租赁天数不能大于购买天数，否则提示RETURNITEM_ERROR
        int rentTime = Integer.parseInt(dateToday) - Integer.parseInt(dateRent);

        Boolean res = businessService.returnItem(uid,itemId);
        if(res & (rentTime > dayRent)) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error(ResponseCodeEnum.RETURNITEM_ERROR.getCode(), ResponseCodeEnum.RETURNITEM_ERROR.getMsg());
        }
    }


    /**
    * @param: uid,itemId,num,days
    * @description: 添加购物车
    * @author: egg
    * @create: 2022/7/4
    * @update: 2022/7/5
    */
    @PostMapping("/business/cart/{itemId}")
    public ResponseResult addItemToCart(@RequestHeader("uid") String uid,@PathVariable("itemId") String itemId,@RequestBody int num,@RequestBody int days){
        Boolean res = businessService.addCart(uid,itemId,num,days);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
    * @param: uid,itemId,num,days
    * @description:更新购物车
    * @author: egg
    * @create: 2022/7/4
    * @update: 2022/7/5
    */
    @PutMapping("/business/cart")
    public ResponseResult updateCart(@RequestHeader("uid") String uid,@RequestBody String itemId, @RequestBody int num,@RequestBody int days){
        Boolean res = businessService.updateCart(uid,itemId,num,days);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
    * @param: uid,itemId
    * @description: 将选中商品移除购物车
    * @author: egg
    * @create: 2022/7/4
    */
    @DeleteMapping("/business/cart/{itemId}")
    public ResponseResult removeItemFromCart(@RequestHeader("uid") String uid,@PathVariable("itemId") String itemId){
        Boolean res = businessService.removeItemFromCartByItemId(uid,itemId);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
    * @param: uid
    * @description: 获取该用户的购物车
    * @author: egg
    * @create: 2022/7/4
    */
    @GetMapping("/business/cart/")
    public ResponseResult getCartByUid(@RequestHeader("uid") String uid){
        List<Cart> responseData = businessService.getCartByUid(uid);
        if(responseData == null) {
            return ResponseResult.error();
        }
        else {
            return ResponseResult.success(responseData);
        }
    }

    /**
    * @param:
    * @description: 订单支付（待完善）
    * @author: egg
    * @create: 2022/7/6
    */
    @GetMapping("/business/order/{orderId}/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public String pay(AliPay aliPay) {
        AlipayTradePagePayResponse response;
        try {
            //  发起API调用（以创建当面付收款二维码为例）
            response = Factory.Payment.Page()
                    .pay(URLEncoder.encode(aliPay.getSubject(), "UTF-8"), aliPay.getTraceNo(), String.valueOf(aliPay.getTotalAmount()), "");
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.getBody();
    }

    @PostMapping("/business/order/{orderId}/notify")  // 注意这里必须是POST接口
    public ResponseResult payNotify(@RequestHeader("uid") String uid,@PathVariable("orderId") String orderID, HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));

                // 更新订单为已支付
                PayRes= orderClient.updateOrderStatus(uid,orderID,2);

            }
        }
        if(PayRes) {
            return ResponseResult.success();
        }
        else
            return ResponseResult.error();
    }

}

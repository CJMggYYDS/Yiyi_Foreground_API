package com.yiyi_app.controller;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.yiyi_app.dto.AliPay;
import com.yiyi_app.entity.*;

import com.yiyi_app.service.BusinessService;
import com.yiyi_app.service.client.OrderClient;
import com.yiyi_app.util.ResponseCodeEnum;
import com.yiyi_app.util.ResponseResult;
import com.yiyi_app.vo.CartVO;
import com.yiyi_app.vo.RentItemListVO;
import com.yiyi_app.vo.RentVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    public static double price;


    /**
    * @param:
    * @description: 租赁商品直接下单
    * @author: egg
    * @create: 2022/7/4
    */
    @PostMapping("/business/order")
    public ResponseResult rentItem(@RequestHeader("uid") String uid,@RequestBody Map<String, Object> map){
        List<Object> list = (ArrayList<Object>) map.get("itemList");
        List<RentItemListVO> rentItemListVO = new ArrayList<>();
        RentItemListVO itemList = null;
        for (Object object : list) {
            itemList = new RentItemListVO();
            itemList.setItemId(((LinkedHashMap<Object, Object>) object).get("itemId").toString());
            itemList.setDays(Integer.parseInt(((LinkedHashMap<Object, Object>) object).get("days").toString()));
            itemList.setNum(Integer.parseInt(((LinkedHashMap<Object, Object>) object).get("num").toString()));
            rentItemListVO.add(itemList);
        }

        RentVO rentVO = new RentVO();
        rentVO.setUid(uid);
        String orderID = UUID.randomUUID().toString().substring(1,7);
        rentVO.setOrderId(orderID);
        rentVO.setTimestamp(map.get("timestamp").toString());
        rentVO.setAddress(map.get("address").toString());
        rentVO.setItemList(rentItemListVO);
        rentVO.setOrderliststatus(1);
        rentVO.setOrderstatus(rentVO.getItemList().size());
        System.out.println(rentItemListVO);
        System.out.println("rentVO"+rentVO);
        Boolean res = businessService.rentItem(rentVO);
        System.out.println("res"+res);
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
    public ResponseResult returnItem(@RequestParam("orderId") String orderId,@RequestParam("itemId") String itemId){
        
        //获取当前日期
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateToday = date.format(formatter).trim().replaceAll("-", "");
        System.out.println("dateToday"+dateToday);
        //获取租赁购买的天数
        Orderlist orderlist = orderClient.getOrderListById(orderId,itemId);
        int dayRent = orderlist.getDays();
        System.out.println("dayRent"+dayRent);
        //获取下单日期
        Orders orders = orderClient.getOrdersByOrderId(orderId);
        String dateRent = orders.getOrdertime();
        System.out.println("dateRent"+dateRent);

        //计算租赁服装天数，租赁天数不能大于购买天数，否则提示RETURNITEM_ERROR
        int rentTime = Integer.parseInt(dateToday) - Integer.parseInt(dateRent);
        System.out.println("rentTime"+rentTime);

        Boolean res = businessService.returnItem(orderId,itemId);
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
    public ResponseResult addItemToCart(@RequestHeader("uid") String uid,@PathVariable("itemId") String itemId,@RequestParam int num,@RequestParam int days){
        Boolean res = businessService.addCart(uid,itemId,num,days);
        System.out.println("添加购物车 res"+res);
        if(res) {
            return ResponseResult.success();
        }
        else {
            return ResponseResult.error();
        }
    }

    /**
    * @param: uid,itemId,num,days
    * @description: 更新购物车
    * @author: egg
    * @create: 2022/7/4
    * @update: 2022/7/5
    */
    @PostMapping("/business/cart")
    public ResponseResult updateCart(@RequestHeader("uid") String uid,@RequestParam String itemId, @RequestParam int num,@RequestParam int days){
        Boolean res = businessService.updateCart(uid,itemId,num,days);
        System.out.println("更新购物车 res"+res);
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
        System.out.println("移除购物车 res"+res);
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
        List<CartVO> responseData = businessService.getCartByUid(uid);
        System.out.println("获取该用户的购物车"+responseData);
        if(responseData == null) {
            return ResponseResult.error();
        }
        else {
            return ResponseResult.success(responseData);
        }
    }

    /**
    * @param:
    * @description: 订单支付
    * @author: egg
    * @create: 2022/7/6
     * 沙箱账号：bshkch2085@sandbox.com ; 密码：111111
    */
    @GetMapping("/business/order/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public String pay(AliPay aliPay) {
        AlipayTradePagePayResponse response;
        try {
            //  发起API调用（以创建当面付收款二维码为例）
            response = Factory.Payment.Page()
                    .pay(aliPay.getSubject(), aliPay.getTraceNo(), String.valueOf(aliPay.getTotalAmount()), "");
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        return response.getBody();
    }

    @PostMapping("/business/order/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String orderId = params.get("out_trade_no");
            List<Orderlist> orderlists= orderClient.getOrderListByOrderId(orderId);

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
                for(int i=0;i<orderlists.size();i++){
                    String itemId = orderlists.get(i).getItemId();
                    //更新订单为已支付
                    orderClient.updateOrderStatus(orderId,itemId,2);
                }
            }
        }
        return "success";
    }

}

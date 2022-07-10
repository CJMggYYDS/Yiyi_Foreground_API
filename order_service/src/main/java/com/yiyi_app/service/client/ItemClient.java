package com.yiyi_app.service.client;

import com.yiyi_app.entity.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *  使用OpenFeign调用item-service微服务
 * @author cjm
 * @date: 2022/7/3
 */
@FeignClient("item-service")
public interface ItemClient {

    @PostMapping("/items/item/getItemsByItemId")
    Item getItemsByItemId(@RequestParam("itemId") String itemId);
}

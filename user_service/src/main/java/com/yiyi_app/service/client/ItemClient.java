package com.yiyi_app.service.client;

import com.yiyi_app.entity.Item;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  使用OpenFeign调用item-service微服务
 * @author cjm
 * @date: 2022/7/3
 */
@FeignClient("item-service")
public interface ItemClient {

    //该接口暂时没有用处
    @GetMapping("/items/item/{itemId}")
    Item getItemByItemId(@PathVariable("itemId") String itemId);

    @PostMapping("/items/item")
    List<Item> getItemsByListId(@RequestBody List<String> itemIds);
}

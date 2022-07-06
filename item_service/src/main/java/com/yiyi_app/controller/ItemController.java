package com.yiyi_app.controller;

import com.yiyi_app.entity.Category;
import com.yiyi_app.entity.Item;
import com.yiyi_app.service.ItemService;
import com.yiyi_app.service.LogService;
import com.yiyi_app.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
public class ItemController {

    @Resource
    ItemService itemService;

    @Resource
    LogService logger;

    /**
     * 搜索商品
     * @param keywords
     * @date: 2022/7/3
     */
    @GetMapping("/items/search")
    public ResponseResult searchItems(@RequestBody Map<String, String> keywords) {
        String keyword = keywords.get("keyword");
        List<Item> responseData = itemService.searchItemsByKeyword(keyword);
        return ResponseResult.success(responseData);
    }

    /**
     * 根据分离检索商品
     * @param classify
     * @date: 2022/7/3
     */
    @GetMapping("/items/{classify}")
    public ResponseResult getItemsByCategory(@PathVariable("classify") String classify) {
        List<Item> responseData = itemService.getItemsByClassify(classify);
        return ResponseResult.success(responseData);
    }

    /**
     * 获取所有分类
     * @date: 2022/7/3
     */
    @GetMapping("/items/categories")
    public ResponseResult getCategories() {
        List<Category> responseData = itemService.getAllCategories();
        return ResponseResult.success(responseData);
    }

    /**
     * 获取单个商品
     * @param itemId
     * @date: 2022/7/3
     */
    @GetMapping("/items/item/{itemId}")
    public ResponseResult getItem(@PathVariable("itemId") String itemId, @RequestHeader("uid") String uid){
        Item item = itemService.getItemByItemId(itemId);
        if(item!=null && uid!=null) {
            logger.addBrowseAction(uid, itemId);
        }
        return ResponseResult.success(item);
    }

    /**
     * 用于远程调用的接口
     * @param itemIds
     * @date: 2022/7/5
     */
    @PostMapping("/items/item")
    public List<Item> getItemsByListId(@RequestBody List<String> itemIds) {
        return itemService.getItemsByIdList(itemIds);
    }


    /**
    * @param: itemId
    * @description: 用于获取item远程调用的接口
    * @author: egg
    * @create: 2022/7/4
    */
    @PostMapping("/items/item")
    public Item getItemsByItemId(@RequestBody String itemId) {
        return itemService.getItemByItemId(itemId);
    }

    /**
     *  针对不同用户喜好推荐服务
     *  url: /items/user/{uid} (暂定)
     *
     */
}

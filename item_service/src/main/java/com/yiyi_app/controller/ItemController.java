package com.yiyi_app.controller;

import com.yiyi_app.entity.Category;
import com.yiyi_app.entity.Item;
import com.yiyi_app.entity.ItemTime;
import com.yiyi_app.recommend.dto.UserActiveDTO;
import com.yiyi_app.recommend.dto.UserSimilarityDTO;
import com.yiyi_app.recommend.service.UserActiveService;
import com.yiyi_app.recommend.service.UserSimilarityService;
import com.yiyi_app.recommend.util.RecommendUtils;
import com.yiyi_app.service.ItemService;
import com.yiyi_app.service.LogService;
import com.yiyi_app.util.ResponseResult;
import com.yiyi_app.vo.ItemTimeVO;
import com.yiyi_app.vo.ItemVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class ItemController {

    @Resource
    ItemService itemService;

    @Resource
    LogService logger;

    @Resource
    UserSimilarityService userSimilarityService;

    @Resource
    UserActiveService userActiveService;

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
     * 根据分类检索商品
     * @param classify
     * @date: 2022/7/3
     */
    @GetMapping("/items/classify/{classify}")
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
        ItemVO item = itemService.getItemVOByItemId(itemId);
        if(item!=null && (!Objects.equals(uid, "未登录"))) {
            logger.addBrowseAction(uid, itemId);
        }
        return ResponseResult.success(item);
    }

    /**
     * 获取销量top10的item
     * @date: 2022/7/6
     */
    @GetMapping("/items/hotItems")
    public ResponseResult getTop10Items() {
        return ResponseResult.success(itemService.getTop10Items());
    }


    /**
     *  针对不同用户喜好推荐服务
     *  基于用户协同过滤算法
     * @param uid
     * @date: 2022/7/6
     */
    @GetMapping("/items/user/{uid}")
    public ResponseResult getRecommendItems(@PathVariable("uid") String uid) {
        List<UserSimilarityDTO> userSimilarityDTOList=userSimilarityService.getListUserSimilarityByUID(uid);

        //System.out.println("SimilarityDTO: "+userSimilarityDTOList);//

        int topN=5;
        List<String> userUIDs= RecommendUtils.getSimilarityTopN(uid, userSimilarityDTOList, topN);

        //System.out.println("topN:" + userUIDs);//

        List<UserActiveDTO> userActiveDTOList=userActiveService.listAllUserActive();

        //System.out.println("userActiveDTO: "+userActiveDTOList);//

        List<String> recommendItems1=RecommendUtils.getRecommendItems(uid, userUIDs, userActiveDTOList);

        //System.out.println(recommendItems1);//1

        List<String> top10Items=itemService.getTop10ItemsID();

        Collections.shuffle(top10Items);
        int randomLength=4;
        List<String> recommendItems2=top10Items.subList(0, randomLength);

        recommendItems1.addAll(recommendItems2);

        List<String> recommendItems= recommendItems1
                .stream()
                .distinct()
                .collect(Collectors.toList());

        //System.out.println(recommendItems);//5

        return ResponseResult.success(itemService.getItemsByIdList(recommendItems));
    }

    /**
     * 用于远程调用的接口
     * @param itemIds
     * @date: 2022/7/5
     */
    @PostMapping("/items/itemvo")
    public List<ItemVO> getItemsByListId(@RequestBody List<String> itemIds) {
        return itemService.getItemsByIdList(itemIds);
    }


    /**
    * @param: itemId
    * @description: 用于获取item远程调用的接口
    * @author: egg
    * @create: 2022/7/4
    */
    @PostMapping("/items/item/getItemsByItemId")
    public Item getItemsByItemId(@RequestParam String itemId) {
        System.out.println("itemId"+itemId);
        return itemService.getItemByItemId(itemId);
    }

    /**
     * 用于远程调用的接口
     * @param itemTimes
     * @date: 2022/7/8
     */
    @PostMapping("/items/timeItem")
    public List<ItemTimeVO> getItemTimeVOsByList(@RequestBody List<ItemTime> itemTimes) {
        return itemService.getItemsTimeVOByIdList(itemTimes);
    }

}

package com.yiyi_app.service;

import com.yiyi_app.entity.Category;
import com.yiyi_app.entity.Item;
import com.yiyi_app.entity.ItemTime;
import com.yiyi_app.vo.ItemTimeVO;
import com.yiyi_app.vo.ItemVO;

import java.util.List;

public interface ItemService {

    Category getCategoryByClassify(String classify);

    List<Category> getAllCategories();

    ItemVO getItemVOByItemId(String itemId);

    Item getItemByItemId(String itemId);

    List<Item> getItemsByClassify(String classify);

    List<Item> searchItemsByKeyword(String keyword);

    List<ItemVO> getItemsByIdList(List<String> items);

    List<ItemVO> getTop10Items();

    List<String> getTop10ItemsID();

    List<ItemTimeVO> getItemsTimeVOByIdList(List<ItemTime> itemTimes);
}

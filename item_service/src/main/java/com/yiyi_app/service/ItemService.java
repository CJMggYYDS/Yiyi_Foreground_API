package com.yiyi_app.service;

import com.yiyi_app.entity.Category;
import com.yiyi_app.entity.Item;

import java.util.List;

public interface ItemService {

    Category getCategoryByClassify(String classify);

    List<Category> getAllCategories();

    Item getItemByItemId(String itemId);

    List<Item> getItemsByClassify(String classify);

    List<Item> searchItemsByKeyword(String keyword);

    List<Item> getItemsByIdList(List<String> items);
}

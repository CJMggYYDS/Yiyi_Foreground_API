package com.yiyi_app.service.impl;

import com.yiyi_app.entity.Category;
import com.yiyi_app.entity.Item;
import com.yiyi_app.mapper.CategoryMapper;
import com.yiyi_app.mapper.ItemMapper;
import com.yiyi_app.service.ItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Resource
    ItemMapper itemMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public Category getCategoryByClassify(String classify) {
        return categoryMapper.getCategoryByClassify(classify);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.getAllCategoryClassify();
    }

    @Override
    public Item getItemByItemId(String itemId) {
        return itemMapper.getItemByItemId(itemId);
    }

    @Override
    public List<Item> getItemsByClassify(String classify) {
        return itemMapper.getItemsByClassify(classify);
    }

    @Override
    public List<Item> searchItemsByKeyword(String keyword) {
        return itemMapper.searchItemsByKeyword("%"+keyword.toLowerCase()+"%");
    }

    @Override
    public List<Item> getItemsByIdList(List<String> items) {
        List<Item> results=new ArrayList<>();
        items.forEach(itemId -> {
            Item item=itemMapper.getItemByItemId(itemId);
            results.add(item);
        });
        return results;
    }
}

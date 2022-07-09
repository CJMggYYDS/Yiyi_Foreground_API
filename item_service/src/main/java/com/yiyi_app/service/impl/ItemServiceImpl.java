package com.yiyi_app.service.impl;

import com.yiyi_app.entity.Category;
import com.yiyi_app.entity.Item;
import com.yiyi_app.entity.ItemTime;
import com.yiyi_app.mapper.CategoryMapper;
import com.yiyi_app.mapper.ItemMapper;
import com.yiyi_app.service.ItemService;
import com.yiyi_app.vo.ItemTimeVO;
import com.yiyi_app.vo.ItemVO;
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
    public ItemVO getItemByItemId(String itemId) {
        if(itemId!=null) {
            Item item=itemMapper.getItemByItemId(itemId);
            Integer sales=itemMapper.getSalesByItemId(itemId);
            return new ItemVO(item.getItemId(),item.getClassify(),item.getItemName(),item.getPrice(),item.getUrl(),item.getInventory(),sales);
        }
        return null;
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
    public List<ItemVO> getItemsByIdList(List<String> items) {
        List<ItemVO> results=new ArrayList<>();
        items.forEach(itemId -> {
            ItemVO itemVO=getItemByItemId(itemId);
            results.add(itemVO);
        });
        return results;
    }

    @Override
    public List<ItemVO> getTop10Items() {
        List<String> itemIds=itemMapper.getItemsSalesTop10();
        return getItemsByIdList(itemIds);
    }

    @Override
    public List<String> getTop10ItemsID() {
        return itemMapper.getItemsSalesTop10();
    }

    @Override
    public List<ItemTimeVO> getItemsTimeVOByIdList(List<ItemTime> itemTimes) {
        List<ItemTimeVO> results=new ArrayList<>();
        itemTimes.forEach(itemTime -> {
            ItemVO itemVO=getItemByItemId(itemTime.getItemId());
            ItemTimeVO itemTimeVO=new ItemTimeVO(itemVO.getItemId(), itemVO.getClassify(), itemVO.getItemName(), itemVO.getPrice(), itemVO.getUrl(), itemVO.getInventory(), itemVO.getSales(), itemTime.getTimestamp());
            results.add(itemTimeVO);
        });
        return results;
    }
}

package com.yiyi_app.mapper;

import com.yiyi_app.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Select("select itemid,classify,itemName,price,description,url,inventory from shareclothes.item where itemid=#{itemId}")
    Item getItemByItemId(String itemId);

    @Select("select itemid,classify,itemName,price,description,url,inventory from shareclothes.item where classify=#{classify}")
    List<Item> getItemsByClassify(String classify);

    @Select("select itemid,classify,itemName,price,description,url,inventory from shareclothes.item where lower(itemName) like #{keyword}")
    List<Item> searchItemsByKeyword(String keyword);

    @Select("select sales from shareclothes.itemsales where itemid=#{itemId}")
    Integer getSalesByItemId(String itemId);

    @Select("select itemId from shareclothes.itemsales order by sales desc limit 10")
    List<String> getItemsSalesTop10();
}

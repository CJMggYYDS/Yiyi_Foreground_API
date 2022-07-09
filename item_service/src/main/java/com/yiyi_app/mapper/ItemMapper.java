package com.yiyi_app.mapper;

import com.yiyi_app.entity.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper {

    @Select("select * from yiyi_db_v2.item where itemid=#{itemId}")
    Item getItemByItemId(String itemId);

    @Select("select * from yiyi_db_v2.item where classify=#{classify}")
    List<Item> getItemsByClassify(String classify);

    @Select("select * from yiyi_db_v2.item where lower(itemName) like #{keyword}")
    List<Item> searchItemsByKeyword(String keyword);

    @Select("select sales from yiyi_db_v2.itemsales where itemid=#{itemId}")
    Integer getSalesByItemId(String itemId);

    @Select("select itemId from yiyi_db_v2.itemsales order by sales desc limit 10")
    List<String> getItemsSalesTop10();
}

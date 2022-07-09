package com.yiyi_app.mapper;

import com.yiyi_app.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    @Select("select * from shareclothes.category")
    List<Category> getAllCategoryClassify();

    @Select("select * from shareclothes.category where classify=#{classify}")
    Category getCategoryByClassify(String classify);
}

package com.yiyi_app.mapper;

import com.yiyi_app.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select password from shareclothes.user where username = #{username}")
    String getPasswordByUsername(String username);

    @Select("select * from shareclothes.user where username = #{username}")
    User getUserDetailByUsername(String username);
}

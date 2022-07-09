package com.yiyi_app.mapper;

import com.yiyi_app.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("select * from shareclothes.user where username = #{username}")
    User getUserByUsername(String username);

    @Select("select * from shareclothes.user where uid=#{uid}")
    User getUserByUID(String uid);

    @Insert("insert into shareclothes.user(uid, username, password, address, status, favoritesId) " +
            "VALUES (#{uid}, #{username}, #{password}, #{address}, #{status}, #{favoritesId})")
    int insertUser(User user);

    @Update("update shareclothes.user set address=#{user.address}, favoritesId=#{user.favoritesId} where username=#{user.username}")
    int updateUser(@Param("user") User user);

}

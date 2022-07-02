package com.yiyi_app.mapper;

import com.yiyi_app.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

    @Select("select * from yiyi_db_v2.user where username = #{username}")
    User getUserByUsername(String username);

    @Select("select * from yiyi_db_v2.user where uid=#{uid}")
    User getUserByUID(String uid);

    @Insert("insert into yiyi_db_v2.user(uid, username, password, address, status, favoritesId) " +
            "VALUES (#{uid}, #{username}, #{password}, #{address}, #{status}, #{favoritesId})")
    int insertUser(User user);

    @Update("update yiyi_db_v2.user set address=#{user.address}, favoritesId=#{user.favoritesId} where username=#{user.username}")
    int updateUser(User user);

}

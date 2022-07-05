package com.yiyi_app.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LogMapper {

    @Insert("insert into yiyi_db_v2.log(uid, timestamp, action, itemId) values (#{uid}, #{timestamp}, #{action}, #{itemId})")
    int insertUserAction(@Param("uid") String uid, @Param("timestamp") String timestamp, @Param("action") int action, @Param("itemId") String itemId);

    @Delete("delete from yiyi_db_v2.log where uid=#{uid} and action=#{action} and itemId=#{itemId}")
    int deleteAction(@Param("uid") String uid, @Param("action") int action, @Param("itemId") String itemId);

    @Select("select yiyi_db_v2.log.itemId from yiyi_db_v2.log where uid=#{uid} and action=#{action}")
    List<String> selectItemsFromLogByUId(@Param("uid") String uid, @Param("action") int action);
}
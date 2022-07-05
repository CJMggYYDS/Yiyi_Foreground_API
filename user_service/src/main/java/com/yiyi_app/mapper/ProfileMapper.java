package com.yiyi_app.mapper;

import com.yiyi_app.entity.Profile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProfileMapper {

    @Select("select * from  yiyi_db_v2.profile where uid=#{uid}")
    List<Profile> getProfileByUserId(String uid);

    @Select("select yiyi_db_v2.profile.itemId from yiyi_db_v2.profile where uid=#{uid}")
    List<String> getItemIdsByUID(String uid);

    @Insert("insert into yiyi_db_v2.profile(uid, itemId) VALUES (#{uid}, #{itemId})")
    int insertProfileByUserIdAndItemId(@Param("uid") String uid, @Param("itemId") String itemId);

    @Delete("delete from yiyi_db_v2.profile where uid=#{uid} and itemId=#{itemId}")
    int deleteProfileByUserIdAndItemId(@Param("uid") String uid, @Param("itemId") String itemId);
}

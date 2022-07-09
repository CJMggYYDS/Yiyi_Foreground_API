package com.yiyi_app.mapper;

import com.yiyi_app.entity.Profile;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProfileMapper {

    @Select("select * from  shareclothes.profile where uid=#{uid}")
    List<Profile> getProfileByUserId(String uid);

    @Select("select itemId from shareclothes.profile where uid=#{uid}")
    List<String> getItemIdsByUID(String uid);

    @Insert("insert into shareclothes.profile(uid, itemId) VALUES (#{uid}, #{itemId})")
    int insertProfileByUserIdAndItemId(@Param("uid") String uid, @Param("itemId") String itemId);

    @Delete("delete from shareclothes.profile where uid=#{uid} and itemId=#{itemId}")
    int deleteProfileByUserIdAndItemId(@Param("uid") String uid, @Param("itemId") String itemId);
}

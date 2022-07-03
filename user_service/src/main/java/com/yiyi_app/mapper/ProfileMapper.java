package com.yiyi_app.mapper;

import com.yiyi_app.entity.Profile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProfileMapper {

    @Select("select * from  yiyi_db_v2.profile where uid=#{uid}")
    List<Profile> getProfileByUserId(String uid);

    @Insert("insert into yiyi_db_v2.profile(uid, itemId) VALUES (#{uid}, #{itemId})")
    int insertProfileByUserIdAndItemId(String uid, String itemId);

    @Delete("delete from yiyi_db_v2.profile where uid=#{uid} and itemId=#{itemId}")
    int deleteProfileByUserIdAndItemId(String uid, String itemId);
}

package com.yiyi_app.mapper;

import com.yiyi_app.entity.Profile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProfileMapper {

    @Select("select * from  yiyi_db_v2.profile where uid=#{uid}")
    Profile getProfileByUserId(String uid);
}

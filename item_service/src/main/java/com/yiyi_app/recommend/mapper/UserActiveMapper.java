package com.yiyi_app.recommend.mapper;

import com.yiyi_app.recommend.dto.UserActiveDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 *  定义操作数据库中用户行为数据的接口
 * @author cjm
 * @date: 2022/7/6
 */
@Mapper
public interface UserActiveMapper {

    // 获得用户对某一商品操作的次数
    @Select("select count(*) from shareclothes.log where uid=#{uid} and itemId=#{itemId}")
    Long getHitsFromUserLog(@Param("uid") String uid, @Param("itemId") String itemId);

    @Select("select uid from shareclothes.log group by uid")
    List<String> getAllUidFromLog();

    @Select("select uid, itemId, count(*) as hits from shareclothes.log group by uid,itemId")
    List<UserActiveDTO> listAllUserActiveDTO();

}

package com.yiyi_app.recommend.mapper;

import com.yiyi_app.recommend.dto.UserSimilarityDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserSimilarityMapper {

    @Insert("insert into yiyi_db_v2.user_similarity values (#{uid}, #{ref_uid}, #{similarity})")
    int insertUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    @Update("update yiyi_db_v2.user_similarity set similarity=#{similarity} where uid=#{uid} and ref_uid=#{ref_uid}")
    int updateUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    @Select("select count(*) from yiyi_db_v2.user_similarity where uid=#{uid} and ref_uid=#{ref_uid} or ref_uid=#{uid} and uid=#{ref_uid}")
    int isUserSimilarity(UserSimilarityDTO userSimilarityDTO);

    @Select("select * from yiyi_db_v2.user_similarity where uid=#{uid} or ref_uid=#{uid}")
    List<UserSimilarityDTO> listUserSimilarityById(String uid);

    @Select("select * from yiyi_db_v2.user_similarity where uid=#{uid} and ref_uid=#{ref_uid}")
    UserSimilarityDTO getUserSimilarityById(@Param("uid") String uid, @Param("ref_uid") String ref_uid);
}

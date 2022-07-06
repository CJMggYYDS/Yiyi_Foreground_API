package com.yiyi_app.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiyi_app.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CartMapper extends BaseMapper<Cart> {
}

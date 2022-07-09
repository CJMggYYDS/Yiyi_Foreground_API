package com.yiyi_app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiyi_app.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrdersMapper extends BaseMapper<Orders> {
}

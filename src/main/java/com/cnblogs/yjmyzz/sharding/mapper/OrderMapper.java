package com.cnblogs.yjmyzz.sharding.mapper;

import com.cnblogs.yjmyzz.sharding.entity.Order;
import com.cnblogs.yjmyzz.sharding.entity.OrderExample;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OrderMapper extends Mapper<Order> {

    int insertBatch(List<Order> orders);

    int countByExample(OrderExample example);

    int maxOrderIdByExample(OrderExample example);

    int minOrderIdByExample(OrderExample example);

    int maxUserIdByExample(OrderExample example);

    int minUserIdByExample(OrderExample example);

    int deleteByExample(OrderExample example);

    List<Order> selectByExample(OrderExample example);

    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);
}
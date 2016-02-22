package com.cnblogs.yjmyzz.sharding.service.impl;

import com.cnblogs.yjmyzz.sharding.entity.Order;
import com.cnblogs.yjmyzz.sharding.entity.OrderExample;
import com.cnblogs.yjmyzz.sharding.mapper.OrderMapper;
import com.cnblogs.yjmyzz.sharding.service.OrderService;
import com.huijiame.utils.list.ListUtil;
import com.huijiame.utils.sharding.jdbc.SingleKeyModuloTableShardingAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by yangjunming on 2/16/16.
 * author: yangjunming@huijiame.com
 */
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    SingleKeyModuloTableShardingAlgorithm singleKeyModuloTableShardingAlgorithm;


    @Override
    public List<Order> getAllOrder() {
        return orderMapper.selectByExample(null);
    }


    @Override
    public void addOrder(Order o) {
        orderMapper.insertSelective(o);
    }

    @Override
    public void addOrders(List<Order> orders) {
        Map<String, List<Order>> map = ListUtil.getMapByKeyProperty(orders, "userId");
        for (String userId : map.keySet()) {
            Map<String, List<Order>> map2 = ListUtil.getMapByModKeyProperty(map.get(userId), "orderId",
                    singleKeyModuloTableShardingAlgorithm.getTableCount());
            for (String key : map2.keySet()) {
                orderMapper.insertBatch(map2.get(key));
            }
        }
    }

    @Override
    public void updateOrders(List<Integer> userIds, String newOrderStatus) {
        Order o = new Order();
        o.setStatus(newOrderStatus);
        OrderExample example = new OrderExample();
        example.createCriteria().andUserIdIn(userIds);
        orderMapper.updateByExampleSelective(o, example);
    }

    @Override
    public void deleteAll() {
        orderMapper.deleteByExample(null);
    }

    @Override
    public int getCount(OrderExample example) {
        return orderMapper.countByExample(example);
    }

    @Override
    public void delete(Order order) {
        orderMapper.delete(order);
    }

    @Override
    public void update(Order order) {
        OrderExample example = new OrderExample();
        example.createCriteria()
                .andUserIdEqualTo(order.getUserId())
                .andOrderIdEqualTo(order.getOrderId());
        orderMapper.updateByExampleSelective(order, example);
    }

    @Override
    public int getMaxOrderId(OrderExample example) {
        return orderMapper.maxOrderIdByExample(example);
    }

    @Override
    public int getMinOrderId(OrderExample example) {
        return orderMapper.minOrderIdByExample(example);
    }

    @Override
    public int getMaxUserId(OrderExample example) {
        return orderMapper.maxUserIdByExample(example);
    }

    @Override
    public int getMinUserId(OrderExample example) {
        return orderMapper.minUserIdByExample(example);
    }
}

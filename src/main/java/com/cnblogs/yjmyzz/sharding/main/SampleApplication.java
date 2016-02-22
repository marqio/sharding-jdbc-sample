package com.cnblogs.yjmyzz.sharding.main;

import com.cnblogs.yjmyzz.sharding.entity.Order;
import com.cnblogs.yjmyzz.sharding.entity.OrderExample;
import com.cnblogs.yjmyzz.sharding.service.OrderService;
import com.huijiame.utils.list.ListUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangjunming on 2/16/16.
 * author: yangjunming@huijiame.com
 */
public class SampleApplication {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-database.xml");

        OrderService orderService = ctx.getBean(OrderService.class);
        //int tableCount = ctx.getBean(SingleKeyModuloTableShardingAlgorithm.class).getTableCount();

        basicTest(orderService);
        //writeTest(orderService, tableCount);

        ctx.close();
    }

    private static void basicTest(OrderService orderService) {

        //deleteAll
//        orderService.deleteAll();

        //ADD
//        orderService.addOrder(buildOrder(1, 1, "NEW"));
//        orderService.addOrder(buildOrder(2, 1, "NEW"));
//        orderService.addOrder(buildOrder(3, 1, "NEW"));
//        orderService.addOrder(buildOrder(4, 1, "NEW"));
//
//        orderService.addOrder(buildOrder(1, 2, "NEW"));
//        orderService.addOrder(buildOrder(2, 2, "NEW"));
//        orderService.addOrder(buildOrder(3, 2, "NEW"));
//        orderService.addOrder(buildOrder(4, 2, "NEW"));
//
//        orderService.addOrder(buildOrder(5, 3, "NEW"));
//        orderService.addOrder(buildOrder(6, 3, "NEW"));
//        orderService.addOrder(buildOrder(7, 3, "NEW"));
//        orderService.addOrder(buildOrder(8, 3, "NEW"));

        //update
//        Order o = new Order();
//        o.setOrderId(1);
//        o.setUserId(1);
//        o.setStatus("UPDATED");
//        orderService.update(o);

        //update orders
        //orderService.updateOrders(Lists.newArrayList(1, 2), "UPDATED");

        //get orders
//        List<Order> orders = orderService.getAllOrder();
//        System.out.println("size of orderList :" + orders.size());

        //delete
//        o.setStatus(null);
//        orderService.delete(o);

        //delete All
//        orderService.deleteAll();

        //insert batch
//        orderService.addOrders(orders);

        //getCount
        OrderExample example = new OrderExample();
        int count = orderService.getCount(example);
        System.out.println(String.format("count => %d", count));
//        example.createCriteria()
//                .andUserIdBetween(1, 2)
//                .andOrderIdBetween(2, 4);
//        count = orderService.getCount(example);
//        System.out.println(String.format("count => %d", count));

        //getMaxOrderId
//        int maxOrderId = orderService.getMaxOrderId(null);
//        System.out.println(String.format("maxOrderId => %d", maxOrderId));
//        example = new OrderExample();
//        example.createCriteria()
//                .andUserIdEqualTo(2);
//        maxOrderId = orderService.getMaxOrderId(example);
//        System.out.println(String.format("maxOrderId => %d", maxOrderId));

        //getMinOrderId
//        int minOrderId = orderService.getMinOrderId(null);
//        System.out.println(String.format("minOrderId => %d", minOrderId));
//        example = new OrderExample();
//        example.createCriteria()
//                .andUserIdEqualTo(2);
//        minOrderId = orderService.getMinOrderId(example);
//        System.out.println(String.format("minOrderId => %d", minOrderId));

        //getMaxUserId
//        int maxUserId = orderService.getMaxUserId(null);
//        System.out.println(String.format("maxUserId => %d", maxUserId));
//        example = new OrderExample();
//        example.createCriteria()
//                .andOrderIdBetween(3, 6);
//        maxUserId = orderService.getMaxUserId(example);
//        System.out.println(String.format("maxUserId => %d", maxUserId));

        //getMinUserId
//        int minUserId = orderService.getMinUserId(null);
//        System.out.println(String.format("minUserId => %d", minUserId));
//        example = new OrderExample();
//        example.createCriteria()
//                .andOrderIdBetween(3, 6);
//        minUserId = orderService.getMinUserId(example);
//        System.out.println(String.format("minUserId => %d", minUserId));

        //deleteAll
//        orderService.deleteAll();
    }

    private static void writeTest(OrderService orderService, int tableCount) {

        orderService.deleteAll();

        int max = 300000;
        int split = 10;

        System.out.println("正在测试 逐条insert...");
        //逐条插入测试
        long begin = System.currentTimeMillis();

        for (int i = 1; i <= max; i++) {
            orderService.addOrder(buildOrder(i, (i / split) + 1, "NEW"));
        }

        long end = System.currentTimeMillis();

        long elapsed = end - begin;

        System.out.println(String.format("%d 条记录 逐条insert耗时: %d毫秒 (%f分钟), 平均 %f条/秒", max, elapsed, elapsed / 1000.0f / 60.0f, max / (elapsed / 1000.0f)));

        //DELETE
        orderService.deleteAll();

        //批量插入测试
        System.out.println("\n正在测试 批量insert...");

        List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= max; i++) {
            orders.add(buildOrder(i, (i / split) + 1, "NEW"));
        }
        Map<String, List<Order>> dbMapOrders = ListUtil.getMapByKeyProperty(orders, "userId");

        begin = System.currentTimeMillis();
        for (String userId : dbMapOrders.keySet()) {
            Map<String, List<Order>> tableMapOrders = ListUtil.getMapByModKeyProperty(dbMapOrders.get(userId), "orderId", tableCount);
            for (String key : tableMapOrders.keySet()) {
                orderService.addOrders(tableMapOrders.get(key));
            }
        }

        end = System.currentTimeMillis();

        elapsed = end - begin;

        System.out.println(String.format("%d 条记录 批量insert耗时: %d毫秒 (%f分钟), 平均 %f条/秒", max, elapsed, elapsed / 1000.0f / 60.0f, max / (elapsed / 1000.0f)));

        System.out.println(orderService.getCount(null));
    }

    private static Order buildOrder(int orderId, int userId, String status) {
        Order o = new Order();
        o.setOrderId(orderId);
        o.setUserId(userId);
        o.setStatus(status);
        return o;
    }
}

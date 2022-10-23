package com.atguigu.guli.service.trade.controller.api;


import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.utils.JwtHelper;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author atguigu
 * @since 2022-08-03
 */
@RestController
@RequestMapping("/api/trade/order")
public class ApiOrderController {
    @Autowired
    OrderService orderService;
    //3、根据订单id查询订单详情
    //验证用户身份  使用用户id和订单id一起查询
    @GetMapping("auth/getOrder/{id}")
    public R getOrder(@PathVariable("id")String id , HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>().eq(Order::getId, id)
                .eq(Order::getMemberId, memberId));
        return R.ok().data("item",order);
    }


    //2、查询课程是否购买
    @GetMapping("auth/isBuy/{courseId}")
    public R isBuy(@PathVariable("courseId")String courseId, HttpServletRequest request){
        String id = JwtHelper.getId(request);
        Order order = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getCourseId, courseId)
                .eq(Order::getMemberId, id)
                .eq(Order::getStatus, 1));
        // 1:已购买   0：未购买
        return R.ok().data("isBuy" , order==null?"0":"1");
    }
    
    //1、创建订单返回订单id
    @PostMapping("auth/createOrder/{courseId}")//必须验证登录
    public R createOrder(@PathVariable("courseId")String courseId, HttpServletRequest request){
        String memberId = JwtHelper.getId(request);
        String orderId = orderService.createOrder(courseId,memberId);
        return R.ok().data("id",orderId);
    }

}


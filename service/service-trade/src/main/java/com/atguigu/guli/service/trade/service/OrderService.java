package com.atguigu.guli.service.trade.service;

import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.trade.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-03
 */
public interface OrderService extends IService<Order> {

    String createOrder(String courseId,String memberId);

    R getCodeUrl(String orderId, HttpServletRequest request);

    String callback(HttpServletRequest request);
}

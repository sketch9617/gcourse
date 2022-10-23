package com.atguigu.guli.service.trade.controller.api;

import com.atguigu.guli.common.util.utils.HttpClientUtils;
import com.atguigu.guli.common.util.utils.StreamUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.PayLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/trade/wx")
public class ApiWxPayController {
    @Autowired
    OrderService orderService;
    //2、支付成功的回调接口
    @PostMapping("callback")
    public String callback(HttpServletRequest request){
        return orderService.callback(request);
    }

    //1、获取code_url
    @GetMapping("auth/getCodeUrl/{orderId}")
    public R getCodeUrl(@PathVariable("orderId")String orderId,
                        HttpServletRequest request){
        return orderService.getCodeUrl(orderId ,request);
    }
}

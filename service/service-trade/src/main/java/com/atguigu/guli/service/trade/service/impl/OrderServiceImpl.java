package com.atguigu.guli.service.trade.service.impl;

import com.atguigu.guli.common.util.utils.HttpClientUtils;
import com.atguigu.guli.common.util.utils.OrderNoUtils;
import com.atguigu.guli.common.util.utils.StreamUtils;
import com.atguigu.guli.service.base.dto.CourseDto;
import com.atguigu.guli.service.base.dto.MemberDto;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.trade.config.WxPayProperties;
import com.atguigu.guli.service.trade.entity.Order;
import com.atguigu.guli.service.trade.entity.PayLog;
import com.atguigu.guli.service.trade.feign.EduClient;
import com.atguigu.guli.service.trade.feign.UcenterClient;
import com.atguigu.guli.service.trade.mapper.OrderMapper;
import com.atguigu.guli.service.trade.service.OrderService;
import com.atguigu.guli.service.trade.service.PayLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author atguigu
 * @since 2022-08-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    EduClient eduClient;
    @Autowired
    UcenterClient ucenterClient;
    @Autowired
    PayLogService payLogService;
    @Autowired
    WxPayProperties wxPayProperties;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Override
    public String createOrder(String courseId,String memberId) {
        //1、判断 如果用户已购买支付该课程 返回异常
        // 一个用户只能购买一个课程一次，使用用户id和课程id +已支付的状态 查询数据
        Order order = this.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getCourseId, courseId)
                .eq(Order::getMemberId, memberId));
        if(order!=null && order.getStatus()==1){
            //课程已购买 抛出异常
            throw new GuliException(ResultCodeEnum.ORDER_EXIST_ERROR);
        }
        //更新和新增订单 都需要查询订单需要的课程和会员的数据
        //需要远程访问 edu和ucenter服务查询
        R memberDtoR = ucenterClient.getMemberDto(memberId);
        if(memberDtoR.getCode()!=20000){
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
        R courseDtoR = eduClient.getCourseDto(courseId);
        if(courseDtoR.getCode()!=20000){
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
        }
        Object memberDtoObj = memberDtoR.getData().get("item");
        Object courseDtoObj = courseDtoR.getData().get("item");
        //由于R 的data的map的泛型是String->Object  feign远程访问得到的结果是一个json的话默认转为LinkedHashMap
        System.out.println(memberDtoObj.getClass().getName());
        //将Object(HashMap)转为 MemberDto对象：先将map转为json字符串  再将json字符串转为MemberDto对象
        ObjectMapper objectMapper = new ObjectMapper();
        MemberDto memberDto = objectMapper.convertValue(memberDtoObj, MemberDto.class);
        CourseDto courseDto = objectMapper.convertValue(courseDtoObj, CourseDto.class);
        if(order==null){
            //3、如果用户第一次购买该课程   创建订单保存到数据库  返回订单id
            order = new Order();
        }
        //2、判断 如果用户已下单但是未支付  更新订单信息为最新的 返回订单id
        order.setStatus(0);
        order.setOrderNo(OrderNoUtils.getOrderNo());//订单编号
        //会员数据
        order.setMemberId(memberId);
        order.setNickname(memberDto.getNickname());
        order.setMobile(memberDto.getMobile());
        //订单的课程数据
        order.setCourseTitle(courseDto.getTitle());
        order.setCourseCover(courseDto.getCover());
        //courseDto.getPrice()单元元，需要转为分
        long price = courseDto.getPrice().multiply(new BigDecimal("100")).longValue();
        order.setTotalFee(price);
        order.setTeacherName(courseDto.getTeacherName());
        order.setCourseId(courseId);
        //保存或者更新到数据库
        if(!StringUtils.isEmpty(order.getId())){
            //订单id存在更新
            this.updateById(order);
        }else{
            //新增
            this.save(order);
        }
        //返回订单id
        return order.getId();
    }

    @Override
    public R getCodeUrl(String orderId, HttpServletRequest request) {
        try {
            Order order = this.getById(orderId);
            //发起网络请求访问wx的统一下单的api：
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClientUtils client = new HttpClientUtils(url);
            //为请求准备参数： 由于wx支付v2版本 使用xml传递数据，xml格式不便于封装 可以使用wx提供的工具类将map转为xml文件
            //也可以将 xml转为map
            Map<String,String> map = new HashMap<>();
            map.put("appid",wxPayProperties.getAppid());
            map.put("mch_id",wxPayProperties.getMchid());//商户号： 一个商家在wx支付系统唯一的编号， appid代表该商户在wx注册的唯一的一个应用
            map.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串
            //map.put("sign","");//签名：可以为数据生成签名 防止数据被篡改
            map.put("body",order.getCourseTitle()); // 商品的描述
            map.put("out_trade_no",order.getOrderNo()); //商户订单号：表示我们自己的平台唯一的一个订单的编号
            map.put("total_fee","1");//order.getTotalFee().toString());//订单金额
            map.put("spbill_create_ip", request.getRemoteHost());//用户客户端的ip地址
            //??? 需要使用公网ip 或者 内网穿透工具
            map.put("notify_url",wxPayProperties.getNotifyurl());//回调接口地址:wx平台来访问，如果接口地址使用localhost或者局域网ip wx一定不能访问
            map.put("trade_type","NATIVE"); //对接wx支付的方式

            //使用秘钥对上面的map集合处理生成签名 并将map和签名的结果 一起转为一个xml文档字符串
            String xmlParams = WXPayUtil.generateSignedXml(map, wxPayProperties.getPartnerkey());
            //将请求参数 xml文档设置到post的请求体中
            client.setXmlParam(xmlParams);
            //发起请求
            client.post();
            //获取响应结果
            String content = client.getContent();
            //   System.out.println(content);
            //解析响应结果
            //1、校验签名
            boolean flag = WXPayUtil.isSignatureValid(content, wxPayProperties.getPartnerkey());
            if(!flag){
                //签名验证失败
                throw new GuliException(ResultCodeEnum.PAY_WX_SIGUNATURE_VALID_ERROR);
            }
            //2、将xml转为map 获取code_url响应
            map = WXPayUtil.xmlToMap(content);
            String returnCode = map.get("return_code");
            String resultCode = map.get("result_code");
            if(StringUtils.isEmpty(returnCode)||
                    StringUtils.isEmpty(resultCode)||
                    !returnCode.equals("SUCCESS")||
                    !resultCode.equals("SUCCESS")){
                log.error("获取wx支付二维码失败："+ content);
                throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR);
            }
            //获取codeurl
            String codeUrl = map.get("code_url");
            //返回codeurl
            return R.ok().data("code_url",codeUrl)
                    .data("total_fee", 1)
                    .data("out_trade_no" , order.getOrderNo())
                    .data("courseId",order.getCourseId());//课程id  用户支付成功后可以跳转到课程详情页
        } catch (Exception e) {
//            e.printStackTrace();
            throw new GuliException(ResultCodeEnum.PAY_UNIFIEDORDER_ERROR,e);

        }
    }

    @Override
    public String callback(HttpServletRequest request) {
        //给微信应答的map集合
        Map<String,String> replyMap = new HashMap<>();
        replyMap.put("return_code","FAIL");
        replyMap.put("return_msg","FAIL");
        try {
            //获取请求体中的输入流
            ServletInputStream is = request.getInputStream();
            String xmlStr = StreamUtils.inputStream2String(is, "UTF-8");
//            System.out.println(xmlStr);
            //1、验证签名
            boolean b = WXPayUtil.isSignatureValid(xmlStr, wxPayProperties.getPartnerkey());
            if(!b){
                log.error("签名验证失败："+xmlStr);
                replyMap.put("return_msg","签名错误");
                //签名验证失败
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
            }
            //2、验证处理结果是否成功：
            Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
            String resultCode = map.get("result_code");
            String returnCode = map.get("return_code");
            if(StringUtils.isEmpty(returnCode)||
                    StringUtils.isEmpty(resultCode)||
                    !returnCode.equals("SUCCESS")||
                    !resultCode.equals("SUCCESS")){
                log.error("支付失败："+ xmlStr);
                replyMap.put("return_msg","支付失败");
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
            }
            //3、验证支付金额和订单实际金额是否一致
            String cashFee = map.get("cash_fee");
            String outTradeNo = map.get("out_trade_no");
            //查询订单数据
            Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, outTradeNo));
            if(order==null ||
                    1 != Long.parseLong(cashFee)){
                replyMap.put("return_msg","订单不存在或者支付金额和订单金额不一致");
                log.error("订单不存在或者支付金额和订单金额不一致:" +xmlStr);
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR);
            }
            //4、订单支付成功：
            // 更新订单支付状态
            order.setStatus(1);//1 支付成功状态
            order.setPayType(1);//支付方式 1微信
            this.updateById(order);
            // 保存支付日志
            PayLog payLog = new PayLog();
            payLog.setPayType(1);
            payLog.setTransactionId(map.get("transaction_id"));
            payLog.setTotalFee(order.getTotalFee());
            payLog.setTradeState("SUCCESS");
            payLog.setPayTime(new Date());
            payLog.setOrderNo(order.getOrderNo());
            payLog.setAttr(xmlStr);
            payLogService.save(payLog);
            // 更新课程销量
            //====通过mq发送消息到消息队列
            rabbitTemplate.convertAndSend("guli.order.exchange" ,
                    "order.pay.ok" , new Gson().toJson(order));
            // 给用户添加积分
            replyMap.put("return_code","SUCCESS");
            replyMap.put("return_msg","SUCCESS");
            return WXPayUtil.mapToXml(replyMap);
        } catch (Exception e) {
            try {
                return WXPayUtil.mapToXml(replyMap);
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new GuliException(ResultCodeEnum.PAY_ORDERQUERY_ERROR,e);
            }
        }
    }
}

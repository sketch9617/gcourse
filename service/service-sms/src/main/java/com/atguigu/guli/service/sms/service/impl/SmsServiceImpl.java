package com.atguigu.guli.service.sms.service.impl;

import com.atguigu.guli.common.util.utils.FormUtils;
import com.atguigu.guli.common.util.utils.HttpUtils;
import com.atguigu.guli.common.util.utils.RandomUtils;
import com.atguigu.guli.service.base.consts.ServiceConsts;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import com.atguigu.guli.service.sms.config.SmsProperties;
import com.atguigu.guli.service.sms.service.SmsService;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@EnableConfigurationProperties(SmsProperties.class)
@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    SmsProperties smsProperties;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public void sendMsg(String mobile) {
        //发送短信
        //1、验证手机号码格式
        boolean flag = FormUtils.isMobile(mobile);
        if (!flag) {
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        //2、获取验证码频率判断： 2分钟内只能获取一次，一天内只能获取3次
        if(redisTemplate.hasKey("sms:per:mins:"+mobile)){
            //手机号码2分钟内获取过验证码
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_CONTROL);
        }
        Object obj = redisTemplate.opsForValue().get("sms:per:day:"+mobile);
        if(obj==null){//代表手机号码第一次获取验证码
            //初始化获取验证码的次数
            redisTemplate.opsForValue().set("sms:per:day:"+mobile , 0 , 24,TimeUnit.HOURS);
        }else{
            int count = Integer.parseInt(obj.toString());
            if(count>=3){  //手机号码获取验证码次数>=3
                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR_BUSINESS_LIMIT_DAY_CONTROL);
            }
            //手机号码获取验证码次数>0  <3
        }
        try {

            //3、生成验证码发送
            String code = RandomUtils.getSixBitRandom();
            //短信平台的服务器地址：
            // 请求报文和响应报文的头不支持中文
            //1、请求头参数的map
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + smsProperties.getAppcode());
            //2、请求参数的map：url?mobile=159xxxx9999
            Map<String, String> querys = new HashMap<String, String>();
            querys.put("mobile", mobile);//手机号
            querys.put("param", "code:" + code);//验证码
            querys.put("tpl_id", smsProperties.getTplId());//短信模板id
            //3、请求体参数的map： 转为json存到请求体中
            Map<String, String> bodys = new HashMap<String, String>();
            //发起请求得到响应结果
//            HttpResponse response = HttpUtils.doPost(smsProperties.getHost(),
//                    smsProperties.getPath(), smsProperties.getMethod(), headers, querys, bodys);
//            String resJsonStr = EntityUtils.toString(response.getEntity());
//            Map resMap = new Gson().fromJson(resJsonStr, Map.class);
//            String returnCode = resMap.get("return_code").toString();
//            if(!returnCode.equals("00000")){
//                throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR);
//            }
            //短信发送成功
            //4、缓存验证码到redis中 15分钟
            redisTemplate.opsForValue().set(ServiceConsts.SMS_CODE_PREFIX +mobile , code , 15, TimeUnit.MINUTES);
            //5、更新手机号码获取验证码的 次数频率
            //2分钟内只能获取一次验证码：redis中只要存一个mobile作为key的数据 过期时间2分钟
            redisTemplate.opsForValue().set(ServiceConsts.SMS_PER_MINS_PREFIX+mobile , 1 , 2,TimeUnit.MINUTES);
            //1天内只能获取3次验证码:   incr k ;  如果k存在，在它的值基础上+1，如果k不存在，默认使用0+1
            //验证码发送成功：在之前获取验证码次数基础上+1
            redisTemplate.opsForValue().increment(ServiceConsts.SMS_PER_DAY_PREFIX+mobile);

        } catch (Exception e) {
            throw new GuliException(ResultCodeEnum.SMS_SEND_ERROR,e);
        }

    }
}

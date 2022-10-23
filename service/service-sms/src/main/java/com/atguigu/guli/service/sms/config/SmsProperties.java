package com.atguigu.guli.service.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "aliyun.sms")
public class SmsProperties {

    String host;//: "http://dingxin.market.alicloudapi.com"
    String path;//: "/dx/sendSms"
    String method;//: "POST"
    String appcode;//: "8e45699f00ff4bdbb807664045acd813"
    String tplId;//: "TP1711063"
}

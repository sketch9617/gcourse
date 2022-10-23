package com.atguigu.guli.service.trade.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayProperties {

    String appid; //: wxf913bfa3a2c7eeeb
    String mchid; //: 1543338551   # 商户号
    String partnerkey; //: atguigu3b0kn9g5vAtGuifHQH7X8rKCL   # 秘钥：加签验签使用
    String notifyurl; //: http://ls074ejc73x7.ngrok.xiaomiqiu123.top/api/trade/wx/callback   # 支付成功的回调地址
}

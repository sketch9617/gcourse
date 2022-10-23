package com.atguigu.guli.service.ucenter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
@ConfigurationProperties(prefix = "wx.login")
public class WxLoginProperties {

    String qrconnectUrl;//: https://open.weixin.qq.com/connect/qrconnect
    String appid;//: wxed9954c01bb89b47
    String redirectUri;//: http://localhost:8160/api/ucenter/wx/callback
    String accessTokenUrl;//: https://api.weixin.qq.com/sns/oauth2/access_token
    String appsecret;//: a7482517235173ddb4083788de60b90e
    String userInfoUrl;//: https://api.weixin.qq.com/sns/userinfo
}

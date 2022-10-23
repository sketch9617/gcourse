package com.atguigu.guli.service.vod.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
//容器创建对象时  自动根据前缀和属性名加载属性值设置(通过set方法设置)给该属性
@ConfigurationProperties(prefix = "aliyun.vod")
public class VodProperties {
    String accessKeyId;//: LTAI5tDaEmk7jmQwbvpDgQeS
    String accessKeySecret;//: Tlvngst9OxoXRSDq4iWAjapnk2BXPp
    String workFlowId;//: 2ca20bda021ea3d886926c3fbf7af6b7
    String regionId;//: cn-shanghai
}

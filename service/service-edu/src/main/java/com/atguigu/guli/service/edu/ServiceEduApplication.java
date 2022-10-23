package com.atguigu.guli.service.edu;

import com.atguigu.guli.service.edu.rule.MyRandomRule;
import com.netflix.loadbalancer.IRule;
import feign.Logger;
import feign.Retryer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
//com.atguigu.guli.service.edu
//com.atguigu.guli.service.base
@SpringBootApplication
//@EnableSwagger2 已经抽取到了swagger2Config配置类中
@ComponentScan(basePackages = "com.atguigu.guli.service")
@EnableDiscoveryClient //启动客户端注册自己到注册中心的注解
@EnableFeignClients //扫描创建feignclient对象的注解
@EnableScheduling //启用定时任务
@EnableCaching //启用缓存管理
public class ServiceEduApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceEduApplication.class , args);
    }

    //配置feignclient执行远程访问时输出日志
    @Bean
    public Logger.Level level(){
        //FULL:输出远程访问的全部日志
        return Logger.Level.FULL;//feign客户端输出日志使用logback输出的，日志级别为debug级别
    }

    //重试机制：远程访问失败的重试次数(访问失败重试多少次)
//    @Bean
//    public Retryer retryer(){
//        return new Retryer.Default();
//    }
//    @Bean
//    public IRule myrandomIrule(){
//        return new MyRandomRule();
//    }
}

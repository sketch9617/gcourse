package com.atguigu.guli.service.edu.feign;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
//import com.atguigu.guli.service.edu.feign.fallback.OssClientFallback;
import com.atguigu.guli.service.edu.feign.fallback.OssClientFallback;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "guli-oss",fallback = OssClientFallback.class)//获取要访问的服务器地址:192.168.137.1:8120
//@FeignClient(value = "guli-oss")//获取要访问的服务器地址:192.168.137.1:8120
public interface OssClient {
    @PostMapping("admin/oss/test")//请求方式+请求资源路径
//    public R test(@RequestParam String username,@RequestParam  String password);//形参列表以及传参方式
    //默认 feign客户端将参数以请求体的方式传参
    //@SpringQueryMap 可以将对象转为请求参数的注解
//    public R test(@SpringQueryMap BaseEntity baseEntity);//形参列表以及传参方式
    public R test(@RequestBody BaseEntity baseEntity);//形参列表以及传参方式

    @DeleteMapping("admin/oss/delete")
    public R delete(@RequestParam String path , @RequestParam String module);
}

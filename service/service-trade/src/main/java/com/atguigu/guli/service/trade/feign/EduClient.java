package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "guli-edu")
public interface EduClient {
    @GetMapping("/api/edu/course/getCourseDto/{id}")
    public R getCourseDto(@PathVariable("id")String id);
}

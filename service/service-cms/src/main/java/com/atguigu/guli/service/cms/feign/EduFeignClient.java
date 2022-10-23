package com.atguigu.guli.service.cms.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "guli-edu")
public interface EduFeignClient {
    @GetMapping("/api/edu/teacher/getHotTeachers")
    public R getHotTeachers();

    @GetMapping("/api/edu/course/getHotCourses")
    public R getHotCourses();
}

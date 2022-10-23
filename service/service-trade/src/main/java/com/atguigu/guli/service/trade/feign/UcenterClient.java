package com.atguigu.guli.service.trade.feign;

import com.atguigu.guli.service.base.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "guli-ucenter")
public interface UcenterClient {
    @GetMapping("/api/ucenter/member/getMemberDto/{id}")//远程访问接口 不需要考虑鉴权，我们只考虑用户访问时的权限
    public R getMemberDto(@PathVariable("id")String id);
}

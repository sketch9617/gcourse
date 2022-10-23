package com.atguigu.guli.service.edu.rule;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.RandomUtils;

import java.util.List;

public class MyRandomRule extends RandomRule {

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        System.out.println("要远程访问的服务名："+ key);
//        lb代表负载均衡模块对象，可以根据key获取到远程访问的服务器的ip地址列表
//        lb.getAllServers();
        List<Server> servers = lb.getReachableServers();
        //生成随机索引序号
        int randomIndex = new RandomUtils().nextInt(0, servers.size());
        System.out.println("生成的序号是："+randomIndex+ "挑选的服务器为："+servers.get(randomIndex).getHostPort());
        return servers.get(randomIndex);


    }
}

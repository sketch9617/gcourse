package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.service.base.model.BaseEntity;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.edu.feign.OssClient;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OssClientFallback implements OssClient {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public R test(BaseEntity baseEntity) {
        log.error("ossClient远程调用test失败，参数："+new Gson().toJson(baseEntity));
        return null;
    }
    /*
        日志保存，以后一般需要手动解决问题
        较常用的方案：
            1、mq： 不希望有较大延迟的场景
                删除失败的兜底方法 发送消息到mq的消息队列

                消息队列的消费者 获取消息中的删除失败的头像地址再次尝试删除，如果失败重试后仍失败存到死信队列
            2、redis：适合数据缓存，数据消费由我们自己编程控制
                List:  lpush  rpop   rpush lpop
                删除失败 保存删除失败的信息到redis的list中

                创建一个定时任务 每过1小时获取list中的消息消费，遍历再次尝试删除消息
     */
    @Override
    public R delete(String path, String module) {
        //保存删除失败的头像地址和它的模块
//        log.error("ossClient远程删除讲师头像失败，地址：{},模块：{}",path,module);
        //将删除失败的头像地址写入到redis中
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps("teacher:delfail:avatars");
        hashOps.put(path,module);

        return null;
    }
}

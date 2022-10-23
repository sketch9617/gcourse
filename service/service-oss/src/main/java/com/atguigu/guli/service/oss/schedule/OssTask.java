package com.atguigu.guli.service.oss.schedule;

import com.atguigu.guli.service.oss.service.OssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OssTask {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    OssService ossService;
    //每过1小时执行一次定时任务
    @Scheduled(cron="0 0 * * * ?")
    public void delTeacherAvatars(){
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps("teacher:delfail:avatars");
        if(hashOps.size()==0){
            //集合为空
            return;
        }
        hashOps.entries().forEach((path,module)->{
            try {
                ossService.delete(path,module);
                //文件删除成功：移除redis Hash中的缓存的路径
                hashOps.delete(path);
            } catch (Exception e) {
//                e.printStackTrace();

            }
        });


    }
}

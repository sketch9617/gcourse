package com.atguigu.guli.service.edu.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class EduTask {
    //每过5秒执行一次定时任务
//    @Scheduled(cron = "*/5 * * * * ?")//* 代表所有    */5 代表每过5秒执行一次
//    public void testTask(){
//        log.info("定时任务执行了,当前时间:{}" , new Date());
//    }

}

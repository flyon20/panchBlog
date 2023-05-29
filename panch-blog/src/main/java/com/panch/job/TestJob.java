package com.panch.job;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//定时任务
@Component
public class TestJob {

    @Scheduled(cron = "0/5 * * * * ?")   // 使用cron表达式，每5秒执行一次
    public void testjob(){
        System.out.println("定时任务执行");
    }

}

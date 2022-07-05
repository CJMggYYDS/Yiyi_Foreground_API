package com.yiyi_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *  自定义配置异步任务线程池
 * @author cjm
 * @date: 2022/7/5
 */
@Configuration
public class AsyncServiceConfig {

    @Bean("AsyncActionExecutor")
    public ThreadPoolTaskExecutor asyncOperationExecutor() {
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(8);
        //最大线程数
        executor.setMaxPoolSize(20);
        //队列大小
        executor.setQueueCapacity(Integer.MAX_VALUE);
        //线程活跃时间,单位(s)
        executor.setKeepAliveSeconds(60);
        //线程名设置
        executor.setThreadNamePrefix("AsyncServiceThread-");
        //线程组设置
        executor.setThreadGroupName("AsyncServiceGroup");
        //所有任务结束后关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //初始化线程池
        executor.initialize();

        return executor;
    }
}

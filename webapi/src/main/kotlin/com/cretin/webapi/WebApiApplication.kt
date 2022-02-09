package com.cretin.webapi

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.task.TaskExecutor
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.ThreadPoolExecutor


@SpringBootApplication(scanBasePackages = ["com.cretin"])
@EnableRedisRepositories
@EnableAsync
@MapperScan("com.cretin.webdb.mapper")
@EnableScheduling
class WebApiApplication {
    @Bean(name = ["processExecutor"])
    fun workExecutor(): TaskExecutor? {
//		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//		threadPoolTaskExecutor.setThreadNamePrefix("Async-");
//		threadPoolTaskExecutor.setCorePoolSize(10);
//		threadPoolTaskExecutor.setMaxPoolSize(20);
//		threadPoolTaskExecutor.setQueueCapacity(600);
//		threadPoolTaskExecutor.afterPropertiesSet();
//		return threadPoolTaskExecutor;
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 25 //核心线程数25：线程池创建时候初始化的线程数
        executor.maxPoolSize = 50 //最大线程数50：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        executor.setQueueCapacity(600) //缓冲队列200：用来缓冲执行任务的队列
        executor.keepAliveSeconds = 60 //允许线程的空闲时间60秒：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
        executor.setThreadNamePrefix("taskExecutor-") // 线程池名的前缀：设置好了之后可以方便我们定位处理任务所在的线程池
        //* 线程池对拒绝任务的处理策略：这里采用了CallerRunsPolicy策略，当线程池没有处理能力的时候，该策略会直接在 execute
        //      方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务*//*
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setAwaitTerminationSeconds(60)
        executor.initialize()
        return executor
    }
}

fun main(args: Array<String>) {
    System.setProperty("user.timezone", "Etc/GMT-8")
    runApplication<WebApiApplication>(*args)
}

package amo.lib.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/*
 * 暂未启用,取消@Bean的注释即可启用
 * 覆盖应用全局配置上的Executor
 * 用自定义的线程池,不使用默认的
 */
@Configuration
@EnableAsync
@EnableScheduling
public class ThreadPoolConfig implements AsyncConfigurer {

    private ThreadPoolTaskExecutor executor;

    @Value("${aub.executor.corePoolSize}")
    private Integer corePoolSize;

    @Value("${aub.executor.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${aub.executor.queueCapacity}")
    private Integer queueCapacity;

    @Value("${aub.executor.keepAliveSeconds}")
    private Integer keepAliveSeconds;

    @Value("${aub.executor.threadNamePrefix}")
    private String threadNamePrefix;

    @Autowired
    public ThreadPoolConfig() {
        this.executor = createThreadPoolExecutor();
    }

    /**
     * @Bean托管,需要配置使用
     * 覆盖应用全局配置上的Executor
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return this.executor;
    }

    // @Bean("threadPoolInstance")
    // public ExecutorService createExecutorService() {
    //     return (ExecutorService) this.executor;
    // }

    private ThreadPoolTaskExecutor createThreadPoolExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize == null ? 10 : this.corePoolSize); //核心线程数
        executor.setMaxPoolSize(this.maxPoolSize == null ? 20 : this.maxPoolSize);  //最大线程数
        executor.setQueueCapacity(this.queueCapacity == null ? 1000 : this.queueCapacity); //队列大小
        executor.setKeepAliveSeconds(this.keepAliveSeconds == null ? 300 : this.keepAliveSeconds); //线程最大空闲时间
        executor.setThreadNamePrefix(this.threadNamePrefix == null ? "aub-executor-" : this.threadNamePrefix); //指定用于新创建的线程名称的前缀。
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 拒绝策略
        executor.initialize();

        return executor;
    }

}

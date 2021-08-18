package com.isuper.eden.eve.boot.config;


import com.isuper.eden.eve.boot.common.holder.RequestHolder;
import com.isuper.eden.eve.boot.common.holder.RequestHolderConstant;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @author admin
 */
@Slf4j
@Component
public class TaskConfiguration implements AsyncConfigurer {

    @Resource
    ThreadProperties threadProperties;

    @Override
    public Executor getAsyncExecutor() {
        //Java虚拟机可用的处理器数
        int processors = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        //核心线程数
        executor.setCorePoolSize(this.threadProperties.getCorePoolSize() <= 0 ? processors : this.threadProperties.getCorePoolSize());
        //线程池最大的线程数，只有在缓冲队列满了之后，才会申请超过核心线程数的线程
        executor.setMaxPoolSize(this.threadProperties.getMaxPoolSize());
        //允许线程的空闲时间,当超过了核心线程之外的线程,在空闲时间到达之后会被销毁
        executor.setKeepAliveSeconds(this.threadProperties.getKeepAliveSeconds());
        //用来缓冲执行任务的队列
        executor.setQueueCapacity(this.threadProperties.getQueueCapacity());
        //线程池名的前缀,可以用于定位处理任务所在的线程池
        executor.setThreadNamePrefix(this.threadProperties.getThreadNamePrefix());
        //线程池对拒绝任务的处理策略
        executor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor exe) -> {
            log.warn("当前任务线程池队列已满.");
        });
        executor.setTaskDecorator(new ContextCopyingDecorator());
        //该方法用来设置线程池关闭的时候等待所有任务都完成后,再继续销毁其他的Bean，这样这些异步任务的销毁就会先于数据库连接池对象的销毁。
        executor.setWaitForTasksToCompleteOnShutdown(this.threadProperties.isWaitForTasksToCompleteOnShutdown());
        //该方法用来设置线程池中,任务的等待时间,如果超过这个时间还没有销毁就强制销毁,以确保应用最后能够被关闭,而不是阻塞住。
        executor.setAwaitTerminationSeconds(this.threadProperties.getAwaitTerminationSeconds());

        /**
         * 拒绝策略，默认是AbortPolicy
         * AbortPolicy：丢弃任务并抛出RejectedExecutionException异常
         * DiscardPolicy：丢弃任务但不抛出异常
         * DiscardOldestPolicy：丢弃最旧的处理程序，然后重试，如果执行器关闭，这时丢弃任务
         * CallerRunsPolicy：执行器执行任务失败，则在策略回调方法中执行任务，如果执行器关闭，这时丢弃任务
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());

        executor.initialize();
        return executor;
    }

    @Slf4j
    static class ContextCopyingDecorator implements TaskDecorator {
        @Nonnull
        @Override
        public Runnable decorate(@Nonnull Runnable runnable) {
            //主流程
            String eventId = RequestHolder.get(RequestHolderConstant.REQUEST_EVENT_ID).toString();
            //子线程逻辑
            return () -> {
                try {
                    //将变量重新放入到run线程中。
                    MDC.put(RequestHolderConstant.REQUEST_EVENT_ID, eventId);
                    runnable.run();
                } finally {
                }
            };
        }
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) -> log.error("线程池执行任务发生未知异常.", ex);
    }
}

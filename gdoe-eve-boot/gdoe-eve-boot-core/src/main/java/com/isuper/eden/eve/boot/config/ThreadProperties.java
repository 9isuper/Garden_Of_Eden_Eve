package com.isuper.eden.eve.boot.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * @author admin
 */
@Setter
@Getter
@ToString
@Component
@ConfigurationProperties(prefix = ThreadProperties.TASK_THREAD_PREFIX)
public class ThreadProperties {

    public static final String TASK_THREAD_PREFIX = "gdoe.eve.thread.pool";

    private int corePoolSize = 10;

    private int maxPoolSize = 20;

    private int keepAliveSeconds = 4;

    private int queueCapacity = 512;

    private boolean waitForTasksToCompleteOnShutdown = true;

    private int awaitTerminationSeconds = 60;

    private String threadNamePrefix="taskExecutor-";
}

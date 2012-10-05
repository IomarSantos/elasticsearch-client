package org.elasticsearch.threadpool;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.elasticsearch.common.unit.TimeValue;

public interface ThreadPool {
     
    ThreadPoolInfo info();
    
    ThreadPoolStats stats();
    
    Executor generic();
    
    Executor executor(String name);
    
    ScheduledExecutorService scheduler();
    
    ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, TimeValue interval);
    
    ScheduledFuture<?> schedule(TimeValue delay, String name, Runnable command);
    
    void shutdown();
    
    void shutdownNow();
    
    boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;
}

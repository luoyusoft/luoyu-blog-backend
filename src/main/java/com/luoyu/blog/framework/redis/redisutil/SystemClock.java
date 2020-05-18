package com.luoyu.blog.framework.redis.redisutil;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 高并发场景下System.currentTimeMillis()的性能问题的优化，时间戳打印建议使用
 */
public class SystemClock {

    private static final String THREAD_NAME = "system.clock";

    private static final SystemClock MILLIS_CLOCK = new SystemClock(1);

    private final long precision;

    private final AtomicLong now;

    /**
     * @author jinhaoxun
     * @description 构造器
     * @param precision
     */
    private SystemClock(long precision) {
        this.precision = precision;
        now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    /**
     * @author jinhaoxun
     * @description millisClock方法
     * @return SystemClock
     */
    public static SystemClock millisClock() {
        return MILLIS_CLOCK;
    }

    /**
     * @author jinhaoxun
     * @description 获取Jedis实例方法
     * @return Jedis
     */
    private void scheduleClockUpdating() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, THREAD_NAME);
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), precision, precision, TimeUnit.MILLISECONDS);
    }

    /**
     * @author jinhaoxun
     * @description now方法
     * @return long
     */
    public long now() {
        return now.get();
    }
}
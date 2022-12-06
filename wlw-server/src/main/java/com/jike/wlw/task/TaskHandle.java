package com.jike.wlw.task;

import com.geeker123.rumba.commons.redis.RedisManager;
import com.jike.wlw.core.BaseService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

@Slf4j
public abstract class TaskHandle extends BaseService {

    @Autowired
    private RedisManager redisManager;
    @Autowired
    private Environment env;

    public void doTask(String lockName) {
        String threadName = Thread.currentThread().getName();
        try {
            log.debug("线程({})，开始执行任务了=================", threadName, lockName);
            if (redisManager.acquireLock(lockName)) {
                task();
                redisManager.releaseLock(lockName);
            } else {
                log.warn("线程({})，执行任务:({})，出现并发执行", threadName, lockName);
                Thread.sleep(90000);
                redisManager.releaseLock(lockName);
            }
            log.debug("线程({})，定时任务({})结束了=====================", threadName, lockName);
        } catch (Exception e) {
            log.error("线程({}),定时任务({})执行出错", threadName, lockName, e);
            //释放锁
            redisManager.releaseLock(lockName);
        }
    }

    /**
     * 定时任务内容
     */
    protected abstract void task() throws Exception;
}

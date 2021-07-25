package com.joker17.sql.small.tools.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultipleThreadRunner {

    private CountDownLatch countDownLatch;

    /**
     * task列表
     */
    private final List<Runnable> taskList = new ArrayList<>(32);

    private final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    /**
     * 添加task
     *
     * @param runnable
     */
    public void addTask(Runnable runnable) {
        this.taskList.add(runnable);
    }

    /**
     * 重复添加num次task
     *
     * @param runnable
     * @param num
     */
    public void addTask(Runnable runnable, int num) {
        for (int i = 0; i < num; i++) {
            this.taskList.add(runnable);
        }
    }

    public void addTask(Runnable... runnable) {
        this.taskList.addAll(Arrays.asList(runnable));
    }

    public void addTask(List<Runnable> runnableList) {
        this.taskList.addAll(runnableList);
    }


    /**
     * 执行task
     */
    public void executeTask() {

        executeOnceCheck();

        int size = taskList.size();
        countDownLatch = new CountDownLatch(size);

        for (int i = 0; i < size; i++) {
            startTask(taskList.get(i));
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查只能执行一次
     */
    private void executeOnceCheck() {
        if (!atomicBoolean.compareAndSet(false, true)) {
            throw new IllegalStateException("MultipleThreadRunner has executed.");
        }
    }

    private void startTask(Runnable runnable) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {
                    countDownLatch.countDown();
                }
            }
        }).start();
    }
}

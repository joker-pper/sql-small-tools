package com.joker17.sql.small.tools.support;

import org.springframework.util.Assert;

public class AllocationTaskSupport {

    private int totalSize;

    private int maxThreads;

    private int avgNum;

    private int surplusNum;

    private AllocationTaskSupport(int totalSize, int maxThreads) {
        Assert.isTrue(totalSize > 0, "totalSize must be gt 0");
        Assert.isTrue(maxThreads > 0, "maxThreads must be gt 0");
        Assert.isTrue(totalSize >= maxThreads, "totalSize  must gte maxThreads");

        this.totalSize = totalSize;
        this.maxThreads = maxThreads;
        this.avgNum = totalSize / maxThreads;
        this.surplusNum = totalSize % maxThreads;
    }

    public static AllocationTaskSupport of(int totalSize, int maxThreads) {
        return new AllocationTaskSupport(totalSize, maxThreads);
    }


    /**
     * 获取当前线程index要处理的start index和end index
     *
     * @param threadIndex
     * @return
     */
    public int[] getStartIndexAndEndIndex(int threadIndex) {
        Assert.isTrue(threadIndex < maxThreads, "threadIndex must lt maxThreads");

        int startIndex = threadIndex * avgNum;
        int endIndex = startIndex + avgNum - 1;

        return new int[]{startIndex, endIndex};
    }

    /**
     * 获取当前线程index剩余要处理的index(-1时无需处理)
     *
     * @param threadIndex
     * @return
     */
    public int getSurplusIndex(int threadIndex) {
        Assert.isTrue(threadIndex < maxThreads, "threadIndex must lt maxThreads");
        if (threadIndex < surplusNum) {
            return maxThreads * avgNum + threadIndex;
        }
        return -1;
    }

}

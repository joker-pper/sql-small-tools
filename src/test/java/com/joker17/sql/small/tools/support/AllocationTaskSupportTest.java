package com.joker17.sql.small.tools.support;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class AllocationTaskSupportTest {

    @Test
    public void test1() {
        int totalSize = 10;

        int maxThreads = 2;

        AllocationTaskSupport allocationTaskSupport = AllocationTaskSupport.of(totalSize, maxThreads);

        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(0), new int[]{0, 4}));
        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(1), new int[]{5, 9}));

        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(0), -1);
        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(1), -1);
    }

    @Test
    public void test2() {
        int totalSize = 10;

        int maxThreads = 3;

        AllocationTaskSupport allocationTaskSupport = AllocationTaskSupport.of(totalSize, maxThreads);

        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(0), new int[]{0, 2}));
        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(1), new int[]{3, 5}));
        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(2), new int[]{6, 8}));

        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(0), 9);
        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(1), -1);
        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(2), -1);



        totalSize = 30;

        maxThreads = 4;

        allocationTaskSupport = AllocationTaskSupport.of(totalSize, maxThreads);

        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(0), new int[]{0, 6}));
        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(1), new int[]{7, 13}));
        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(2), new int[]{14, 20}));
        Assert.assertTrue(Arrays.equals(allocationTaskSupport.getStartIndexAndEndIndex(3), new int[]{21, 27}));

        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(0), 28);
        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(1), 29);
        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(2), -1);
        Assert.assertEquals(allocationTaskSupport.getSurplusIndex(3), -1);
    }

}
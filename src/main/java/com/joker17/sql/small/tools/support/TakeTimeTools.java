package com.joker17.sql.small.tools.support;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class TakeTimeTools<T> {

    private final long start;
    private final long end;
    private T result;

    private TakeTimeTools(Supplier<T> supplier) {
        start = System.currentTimeMillis();
        result = supplier.get();
        end = System.currentTimeMillis();
    }

    public static <T> TakeTimeTools of(Supplier<T> supplier) {
        return new TakeTimeTools(supplier);
    }

    public T getResult() {
        return result;
    }

    public long toMillis() {
        return end - start;
    }

    public long toSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(toMillis());
    }

    public String toExactSeconds() {
        long mills = toMillis();
        if (mills == 0) {
            return "0";
        }
        return String.format("%.3f", (double) mills / 1000);
    }

    public long toMinutes() {
        return TimeUnit.MILLISECONDS.toMinutes(toMillis());
    }

    public String toExactMinutes() {
        long mills = toMillis();
        if (mills == 0) {
            return "0";
        }
        return String.format("%.3f", (double) mills / 1000 / 60);
    }

}

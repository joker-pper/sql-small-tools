package com.joker17.sql.small.tools.factory;

import com.joker17.sql.small.tools.executor.AbstractExecutor;
import com.joker17.sql.small.tools.executor.DeleteTableExecutor;
import com.joker17.sql.small.tools.executor.ReplaceTableExecuteExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExecutorFactory {

    private static final Map<String, AbstractExecutor> CACHE_MAP = new HashMap<>(32);

    static {
        register(DeleteTableExecutor.INSTANCE);
        register(ReplaceTableExecuteExecutor.INSTANCE);
    }

    private ExecutorFactory() {
    }

    private static void register(AbstractExecutor executor) {
        CACHE_MAP.put(executor.name(), executor);
    }

    public static AbstractExecutor getInstance(String name) {
        return CACHE_MAP.get(name);
    }

    public static Set<String> getSupportNames() {
        return CACHE_MAP.keySet();
    }


}

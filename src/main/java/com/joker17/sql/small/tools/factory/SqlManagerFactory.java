package com.joker17.sql.small.tools.factory;

import com.joker17.sql.small.tools.manager.TableManager;
import com.joker17.sql.small.tools.manager.impl.TableManagerImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class SqlManagerFactory {

    private static volatile Map<Class, Object> CACHE_MAP = new ConcurrentHashMap<>(32);

    private SqlManagerFactory() {
    }

    public static TableManager getTableManagerInstance() {
        return getInstance(TableManager.class, () -> new TableManagerImpl());
    }

    private static <T> T getInstance(Class<T> classType, Supplier<T> instanceSupplier) {
        T instance = (T) CACHE_MAP.get(classType);
        if (instance == null) {
            synchronized (SqlManagerFactory.class) {
                instance = (T) CACHE_MAP.get(classType);
                if (instance == null) {
                    instance = instanceSupplier.get();
                    CACHE_MAP.put(classType, instance);
                }
            }
        }
        return instance;
    }


}

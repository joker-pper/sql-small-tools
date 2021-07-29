package com.joker17.sql.small.tools;

import com.joker17.sql.small.tools.executor.AbstractExecutor;
import com.joker17.sql.small.tools.factory.ExecutorFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;

public class SqlSmallToolsMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqlSmallToolsMain.class);

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.println(String.format("tools: %s", StringUtils.join(ExecutorFactory.getSupportNames(), "|")));
            return;
        }

        //args[0]作为指定工具的命令
        String toolsName = args[0];
        AbstractExecutor executor = ExecutorFactory.getInstance(toolsName);
        if (executor == null) {
            LOGGER.warn("not found tools named: " + toolsName);
            return;
        }
        executor.execute(Arrays.copyOfRange(args, 1, args.length));
    }

}

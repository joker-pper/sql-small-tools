package com.joker17.sql.small.tools.executor;

import com.beust.jcommander.JCommander;

import java.io.IOException;

public abstract class AbstractExecutor<T extends BaseExecutorParam> {

    /**
     * 名称
     *
     * @return
     */
    public abstract String name();


    /**
     * 解析参数异常
     *
     * @param ex
     */
    protected abstract void commandParseError(RuntimeException ex);


    /**
     * 业务逻辑
     *
     * @param param
     * @throws IOException
     */
    protected abstract void doWork(T param) throws IOException;

    /**
     * 获取参数实例
     *
     * @return
     */
    protected abstract T getParamInstance();


    public void execute(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            args = new String[]{"--help"};
        }

        //解析param
        T param = getParamInstance();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(param)
                .build();
        try {
            jCommander.parse(args);
        } catch (RuntimeException e) {
            commandParseError(e);
            return;
        }

        if (param.isHelp()) {
            jCommander.usage();
            return;
        }
        //执行业务逻辑
        doWork(param);
    }


}

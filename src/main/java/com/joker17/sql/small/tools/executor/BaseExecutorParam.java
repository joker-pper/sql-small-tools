package com.joker17.sql.small.tools.executor;

import com.beust.jcommander.Parameter;

public class BaseExecutorParam {

    @Parameter(names = {"--help", "--h"}, help = true, order = 5)
    private boolean help;

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }
}

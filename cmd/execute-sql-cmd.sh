#!/bin/sh

#进入jar文件所在目录(可视情况去掉)
cd ../target

jar_file=sql-small-tools-1.0.0-SNAPSHOT.jar
data_source=../src/test/resources/db.properties
sql_text=../src/test/resources/execute-sql/execute-sql1.text
max_threads=1

run_command='java -jar '$jar_file' execute-sql -data-source '$data_source' -sql-text '$sql_text' -max-threads '$max_threads

#输出执行命令
echo "run >>>" $run_command

#执行命令
$run_command


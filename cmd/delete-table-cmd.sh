#!/bin/sh

#进入jar文件所在目录(可视情况去掉)
cd ../target

jar_file=sql-small-tools-1.0.0-SNAPSHOT.jar
data_source=../src/test/resources/db.properties
delete_table=../src/test/resources/delete-table/delete-table1.text
max_threads=1

run_command='java -jar '$jar_file' delete-table -data-source '$data_source' -delete-table '$delete_table' -max-threads '$max_threads

#输出执行命令
echo "run >>>" $run_command

#执行命令
$run_command


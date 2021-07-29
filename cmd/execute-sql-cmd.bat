@echo off

::进入jar文件所在目录(可视情况去掉)
cd ../target

set jar-file=sql-small-tools-1.0.0-SNAPSHOT.jar
set data-source=../src/test/resources/db.properties
set sql-text=../src/test/resources/execute-sql/execute-sql1.text
set max-threads=1

set run-command=java -jar %jar-file% execute-sql -data-source %data-source% -sql-text %sql-text% -max-threads %max-threads%

::输出执行命令
echo run ^>^>^> %run-command%
::换行
echo.

::执行命令
%run-command%

pause
# sql-small-tools

    简易批量sql运维小工具(通过spring jdbc template实现)
    理论上支持已添加驱动的数据库

## supports
    MySQL (已验证) | SQLSERVER | ORACLE

## features

+ delete-table 

  - 删除指定的tables,参数:
    - -data-source: 数据源配置文件
    - -delete-table: 删除table配置文件
        - 配置文件参数:
        - type: 1 | 2
        - tables: 根据type值获取tables的配置, type 1:指定具体的tables,多个以,分割 2:指定为查询tables的sql
        - delete-condition: 指定的tables所应用的删除条件
    - -max-threads: 最大线程数
  
  - 命令
  ``` 
   # 通过指定的table列表、删除条件及最大线程数进行执行删除语句
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar delete-table -data-source ../src/test/resources/db.properties -delete-table ../src/test/resources/delete-table/delete-table1.text -max-threads 10
  
   # 通过指定的table查询列表sql、删除条件及最大线程数进行执行删除语句
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar delete-table -data-source ../src/test/resources/db.properties -delete-table ../src/test/resources/delete-table/delete-table2.text -max-threads 10
          
  ``` 
  
+ replace-table-execute

  - 通过自定义sql模板及替换table占位符为指定的tables执行对应的sql(可用于DROP|CREATE|UPDATE|DELETE等),参数:
    - -data-source: 数据源配置文件
    - -replace-table-execute: replace table execute 配置文件
        - 配置文件参数:
        - type: 1 | 2
        - tables: 根据type值获取tables的配置, type 1:指定具体的tables,多个以,分割 2:指定为查询tables的sql
    - -sql-text: sql配置文件, 配置文件内容中table占位符为#{table},支持标记为多组sql(将会合并为一个sql更新执行,其中开始标签为---sql 结束标签为---)
      - 配置文件内容示例:  DROP TABLE IF EXISTS `#{table}`;
    - -max-threads: 最大线程数
  
  - 命令
  ``` 
   # 通过指定的table列表进行执行语句
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar replace-table-execute -data-source ../src/test/resources/db.properties  -replace-table-execute ../src/test/resources/replace-table-execute/replace-table-execute1.text -sql-text ../src/test/resources/replace-table-execute/replace-table-execute-sql.text -max-threads 1
  
   # 通过指定的table查询列表sql进行执行语句
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar replace-table-execute -data-source ../src/test/resources/db.properties  -replace-table-execute ../src/test/resources/replace-table-execute/replace-table-execute2.text -sql-text ../src/test/resources/replace-table-execute/replace-table-execute-sql.text -max-threads 1
         
   # 通过指定的table查询列表sql进行执行语句(包含存储过程创建及调用)
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar replace-table-execute -data-source ../src/test/resources/db.properties  -replace-table-execute ../src/test/resources/replace-table-execute/replace-table-execute2.text -sql-text ../src/test/resources/replace-table-execute/replace-table-execute-sql-with-procedure.text -max-threads 1
          
  ``` 

+ execute-sql

  - 通过自定义sql执行对应的sql,参数:
    - -data-source: 数据源配置文件
    - -sql-text: sql配置文件, 支持多组sql(其中开始标签为---sql 结束标签为---)
    - -max-threads: 最大线程数
  
  - 命令
  ``` 
   # 执行单个sql示例
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar execute-sql -data-source ../src/test/resources/db.properties  -sql-text ../src/test/resources/execute-sql/execute-sql1.text -max-threads 1
  
   # 执行多组sql示例
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar execute-sql -data-source ../src/test/resources/db.properties  -sql-text ../src/test/resources/execute-sql/execute-sql2.text -max-threads 1
     
  ``` 

## other
```
    # mysql数据源连接url参数官方文档
    https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-configuration-properties.html

    # mysql数据源连接url部分参数介绍
    allowMultiQueries=true:
    可以在sql语句后携带分号,实现多语句执行
    https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-connp-props-security.html#cj-conn-prop_allowMultiQueries

    allowPublicKeyRetrieval=true:
    用于解决连接数据库时抛出Public Key Retrieval is not allowed错误
    https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-connp-props-security.html#cj-conn-prop_allowPublicKeyRetrieval

    # 若需尝试运行Readme中的示例命令 (Mysql)
    1. 修改db.properties 
    2. 创建对应的database 
    3. 通过maven test进行初始化表结构
    4. maven package,找到对应的target目录,通过命令行执行对应的命令即可
 
    # 执行出现bad SQL grammar []; nested exception is java.sql.SQLSyntaxErrorException的原因
    1. sql语句存在错误
    2. 存在非法字符,e.g: \uFEFF (一般是转换编码格式为UTF8出现)

``` 
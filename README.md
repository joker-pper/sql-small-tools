# sql-small-tools

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

  - 通过自定义sql模板及替换table占位符为指定的tables执行对应的sql(可用于DROP|CREATE|UPDATE|DELETE),参数:
    - -data-source: 数据源配置文件
    - -replace-table-execute: replace table execute 配置文件
        - 配置文件参数:
        - type: 1 | 2
        - tables: 根据type值获取tables的配置, type 1:指定具体的tables,多个以,分割 2:指定为查询tables的sql
    - -sql-text: sql配置文件, 配置文件内容中table占位符为#{table}
      - 配置文件内容示例:  DROP TABLE IF EXISTS `#{table}`;
    - -max-threads: 最大线程数
  
  - 命令
  ``` 
   # 通过指定的table列表进行执行语句
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar replace-table-execute -data-source ../src/test/resources/db.properties  -replace-table-execute ../src/test/resources/replace-table-execute/replace-table-execute1.text -sql-text ../src/test/resources/replace-table-execute/replace-table-execute-sql.text -max-threads 1
  
   # 通过指定的table查询列表sql进行执行语句
   java -jar sql-small-tools-1.0.0-SNAPSHOT.jar replace-table-execute -data-source ../src/test/resources/db.properties  -replace-table-execute ../src/test/resources/replace-table-execute/replace-table-execute2.text -sql-text ../src/test/resources/replace-table-execute/replace-table-execute-sql.text -max-threads 1
          
  ``` 

## other
```
    # 若需尝试运行Readme中的示例命令
    1. 修改db.properties 
    2. 创建对应的database 
    3. 通过maven test进行初始化表结构
    4. maven package,找到对应的target目录,通过命令行执行对应的命令即可


 
``` 
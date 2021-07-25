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
  
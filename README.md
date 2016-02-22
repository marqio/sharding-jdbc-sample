### 当当[sharding-jdbc](https://github.com/dangdangdotcom/sharding-jdbc)的代码示例

---

**步骤:** 

1. mysql里创建3个数据库,每个库里建二个表(sql脚本参考src/main/sql/init.sql)
2. 修改src/main/resources/properties/jdbc.properties中的连接串
3. 运行src/main/java/com/cnblogs/yjmyzz/sharding/main/SampleApplication.java中的main即可

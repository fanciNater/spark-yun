?> 至轻云中非企业版本功能将全部**开源**，支持本地代码构建部署。

##### 前提

- **CentOS-7.9**
- [Java-1.8](https://ispong.isxcode.com/spring/java/java%20%E5%AE%89%E8%A3%85/)
- [Node-16](https://ispong.isxcode.com/react/nodejs/nodejs%20%E5%AE%89%E8%A3%85/)

##### 下载代码

```bash
git clone https://github.com/isxcode/spark-yun.git
```

##### 下载spark二进制文件

!> 目前只可以使用`spark-3.1.1-bin-hadoop3.2`版本

```bash
wget https://archive.apache.org/dist/spark/spark-3.1.1/spark-3.1.1-bin-hadoop3.2.tgz 
tar vzxf spark-3.1.1-bin-hadoop3.2.tgz -C /tmp/
```

##### 修改配置文件

```bash
vim spark-yun/spark-yun-backend/src/main/resources/application-local.yml
```

```yml
spring:

  security:
    user:
      roles: ADMIN
      name: admin
      password: admin123

  jpa:
    database: mysql
    show-sql: false

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:30306/zhiqingyun
    username: root
    password: ispong123

  flyway:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:30306/zhiqingyun
    user: root
    password: ispong123
    locations: classpath:db/migration/mysql
    enabled: false

  quartz:
    properties:
      org.quartz.dataSource.quartzDataSource.driver: com.mysql.cj.jdbc.Driver
      org.quartz.dataSource.quartzDataSource.URL: jdbc:mysql://localhost:30306/zhiqingyun
      org.quartz.dataSource.quartzDataSource.user: root
      org.quartz.dataSource.quartzDataSource.password: ispong123
```

##### 启动项目

```bash
./gradlew start
```

![img](https://img.isxcode.com/picgo/20230527155307.png)
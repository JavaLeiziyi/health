# health

# 项目概述和环境搭建

# 1. 项目概述

健康管理系统是一款应用于 **健康管理机构 (体检机构) 的业务系统** ，实现健康管理机构工作内容可视化、会员管理专业化、健康评估数字化、健康干预流程化、知识库集成化，从而提高健康管理师的工作效率，加强与会员间的互动，增强管理者对健康管理机构运营情况的了解。

# 2. 技术架构

展示静态原型

## **2.1 技术架构**

### **1. 前端技术栈**

- HTML5: 网页开发**标签语言**
- bootstrap: 前端开发框架,可以实现**响应式布局**
- ElementUI: 基于 Vue 2.0 的**桌面端组件库**
- Vue.js: **构建数据驱动的web界面渐进式框架**
- ajax: 基于**javaScript的异步请求** ,**基于vue的ajax  axios**

### **2. 分布式架构及权限技术栈**

- zookeeper: **分布式小文件系统**, 作为dubbo的服务注册中心
- dubbo: **java RPC框架**, 可以与spring无缝集成
- springMVC: **开源的web层框架**
- springSecurity: **权限管理框架**

### **3. 分布式版本控制及报表技术栈**

- Git: **分布式版本控制系统**  本地仓库和远程仓库  **容错**
- Apache POI: 提供**java对Microsoft Office**格式档案进行读写的**API函式库**
- Echarts: **商业及数据图表**, 纯**javaScript图表库** 百度提供  : 快速生成各类图表

### **4. 持久化技术栈**

- Mybatis: **基于java持久层框架**, 实现了**java代码和sql语句的解耦**
- MySQL: **开源的关系型数据库**

### **5. 第三方服务**

- **阿里云通信:** 短信服务（Short Message Service）**(阿里大于)** 是阿里云为用户提供的一种通信服务的能力。支持国内和国际快速发送验证码、短信通知和推广短信，服务范围覆盖全球200多个国家和地区。国内短信支持三网合一专属通道，与工信部携号转网平台实时互联。电信级运维保障，实时监控自动切换，到达率高达99%。完美支撑双11期间20亿短信发送，6亿用户触达。
- **七牛云:** **分布式对象(文件)存储服务器**  图片(文件)的线上云存储
- **fastDFS: 分布式文件系统**
  - 功能包括：文件存储、文件同步、文件访问（文件上传、文件下载）等，解决了大容量存储和负载均衡的问题。特别适合以文件为载体的在线服务，如相册网站、视频网站等等

![](img\N01_技术架构.png)

## **2.2 功能架构**

**SOA : 面向服务的架构**

![](img\N02_功能架构.png)

## **2.3 软件的开发流程**

软件开发一般会经历如下几个阶段，整个过程是顺序展开，所以通常称为**瀑布模型。**

![](img\N03_开发流程.png)

- **定义阶段**
  - 可行性研究与计划
  - 需求分析: 需求文档: 健康PRD文档.docx
- **开发阶段**
  - 设计: 原型设计, 数据库设计
  - 编码
  - 测试: 解决测试bug
- **维护阶段**
  - 运行维护: 维护两到三年, 之后代价维护

# 3. 环境搭建

## **3.1 项目结构**

![](img\N04_项目结构.png)

各模块职责定位： 

- **health_parent：父工程，打包方式为pom，统一锁定依赖的版本，同时聚合其他子模块便于统一执行maven命令** 
- **health_common：通用模块，打包方式为jar，存放项目中使用到的一些工具类、实体类、返回结果和常量类**
- **health_interface：打包方式为jar，存放服务接口** 
- **health_service_provider：Dubbo服务模块，打包方式为war，存放服务实现类、Dao接口、Mapper映射文件等，作为服务提供方，需要部署到tomcat运行** 
- **health_backend：健康管理后台，打包方式为war，作为Dubbo服务消费方，存放Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行** 
- **health_mobile：移动端前台，打包方式为war，作为Dubbo服务消费方，存放Controller、HTML页面、js、css、spring配置文件等，需要部署到tomcat运行**

## **3.2 maven项目搭建**

本项目分为 **健康管理后台**和传智 **健康前台（微信端）** 

### **3.2.1 health_parent** 

创建health_parent，父工程，打包方式为pom，用于统一管理依赖版本 

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven‐4.0.0.xsd"> 
    <modelVersion>4.0.0</modelVersion> 
    <groupId>com.itheima</groupId> 
    <artifactId>health_parent</artifactId> 
    <version>1.0‐SNAPSHOT</version> 
    <packaging>pom</packaging> 

    <!‐‐ 集中定义依赖版本号 ‐‐> 
    <properties> 
        <junit.version>4.12</junit.version> 
        <spring.version>5.0.5.RELEASE</spring.version> 
        <pagehelper.version>4.1.4</pagehelper.version> 
        <servlet‐api.version>2.5</servlet‐api.version> 
        <dubbo.version>2.6.0</dubbo.version> 
        <zookeeper.version>3.4.7</zookeeper.version> 
        <zkclient.version>0.1</zkclient.version> 
        <mybatis.version>3.4.5</mybatis.version> 
        <mybatis.spring.version>1.3.1</mybatis.spring.version> 
        <mybatis.paginator.version>1.2.15</mybatis.paginator.version> 
        <mysql.version>5.1.32</mysql.version> 
        <druid.version>1.0.9</druid.version> 
        <commons‐fileupload.version>1.3.1</commons‐fileupload.version> 
        <spring.security.version>5.0.5.RELEASE</spring.security.version> 
        <poi.version>3.14</poi.version> 
        <jedis.version>2.9.0</jedis.version> 
        <quartz.version>2.2.1</quartz.version> 
    </properties> 

    <!‐‐ 依赖管理标签 必须加 ‐‐> 
    <dependencyManagement> 
        <dependencies> 
            <!‐‐ Spring ‐‐> 
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐context</artifactId> 
                <version>${spring.version}</version> 
            </dependency> 
            <dependency><groupId>org.springframework</groupId> 
                <artifactId>spring‐beans</artifactId> 
                <version>${spring.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐web</artifactId> 
                <version>${spring.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐webmvc</artifactId> 
                <version>${spring.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐jdbc</artifactId> 
                <version>${spring.version}</version>
            </dependency> 
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐aspects</artifactId> 
                <version>${spring.version}</version> 
            </dependency>
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐jms</artifactId> 
                <version>${spring.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐context‐support</artifactId> 
                <version>${spring.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.springframework</groupId> 
                <artifactId>spring‐test</artifactId> 
                <version>${spring.version}</version> 
            </dependency> 

            <!‐‐ dubbo相关 ‐‐> 
            <dependency><groupId>com.alibaba</groupId> 
                <artifactId>dubbo</artifactId> 
                <version>${dubbo.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.apache.zookeeper</groupId> 
                <artifactId>zookeeper</artifactId> 
                <version>${zookeeper.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>com.github.sgroschupf</groupId> 
                <artifactId>zkclient</artifactId> 
                <version>${zkclient.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>junit</groupId> 
                <artifactId>junit</artifactId> 
                <version>4.12</version> 
            </dependency> 
            <dependency> 
                <groupId>com.alibaba</groupId> 
                <artifactId>fastjson</artifactId> 
                <version>1.2.47</version> 
            </dependency> 
            <dependency> 
                <groupId>javassist</groupId> 
                <artifactId>javassist</artifactId> 
                <version>3.12.1.GA</version> 
            </dependency> 
            <dependency> 
                <groupId>commons‐codec</groupId> 
                <artifactId>commons‐codec</artifactId> 
                <version>1.10</version> 
            </dependency> 
            <dependency> 
                <groupId>com.github.pagehelper</groupId> 
                <artifactId>pagehelper</artifactId> 
                <version>${pagehelper.version}</version> 
            </dependency> 

            <!‐‐ Mybatis ‐‐> 
            <dependency><groupId>org.mybatis</groupId> 
                <artifactId>mybatis</artifactId> 
                <version>${mybatis.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.mybatis</groupId> 
                <artifactId>mybatis‐spring</artifactId> 
                <version>${mybatis.spring.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>com.github.miemiedev</groupId> 
                <artifactId>mybatis‐paginator</artifactId> 
                <version>${mybatis.paginator.version}</version> 
            </dependency> 

            <!‐‐ MySql ‐‐> 
            <dependency> 
                <groupId>mysql</groupId> 
                <artifactId>mysql‐connector‐java</artifactId> 
                <version>${mysql.version}</version> 
            </dependency> 

            <!‐‐ 连接池 ‐‐> 
            <dependency> 
                <groupId>com.alibaba</groupId> 
                <artifactId>druid</artifactId> 
                <version>${druid.version}</version> 
            </dependency> 

            <!‐‐ 文件上传组件 ‐‐> 

            <dependency> 
                <groupId>commons‐fileupload</groupId> 
                <artifactId>commons‐fileupload</artifactId> 
                <version>${commons‐fileupload.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.quartz‐scheduler</groupId> 
                <artifactId>quartz</artifactId> 
                <version>${quartz.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.quartz‐scheduler</groupId> 
                <artifactId>quartz‐jobs</artifactId> 
                <version>${quartz.version}</version></dependency> 
            <dependency> 
                <groupId>com.sun.jersey</groupId> 
                <artifactId>jersey‐client</artifactId> 
                <version>1.18.1</version> 
            </dependency> 
            <dependency> 
                <groupId>com.qiniu</groupId> 
                <artifactId>qiniu‐java‐sdk</artifactId> 
                <version>7.2.0</version> 
            </dependency> 

            <!‐‐POI报表‐‐> 
            <dependency> 
                <groupId>org.apache.poi</groupId> 
                <artifactId>poi</artifactId> 
                <version>${poi.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.apache.poi</groupId> 
                <artifactId>poi‐ooxml</artifactId> 
                <version>${poi.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>redis.clients</groupId> 
                <artifactId>jedis</artifactId> 
                <version>${jedis.version}</version> 
            </dependency> 

            <!‐‐ 安全框架 ‐‐> 
            <dependency> 
                <groupId>org.springframework.security</groupId> 
                <artifactId>spring‐security‐web</artifactId> 
                <version>${spring.security.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.springframework.security</groupId> 
                <artifactId>spring‐security‐config</artifactId> 
                <version>${spring.security.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>org.springframework.security</groupId> 
                <artifactId>spring‐security‐taglibs</artifactId>
                <version>${spring.security.version}</version> 
            </dependency> 
            <dependency> 
                <groupId>com.github.penggle</groupId> 
                <artifactId>kaptcha</artifactId> 
                <version>2.3.2</version> 
                <exclusions> 
                    <exclusion> 
                        <groupId>javax.servlet</groupId> 
                        <artifactId>javax.servlet‐api</artifactId> 
                    </exclusion> 
                </exclusions> 
            </dependency> 
            <dependency> 
                <groupId>dom4j</groupId> 
                <artifactId>dom4j</artifactId> 
                <version>1.6.1</version> 
            </dependency>
            <dependency> 
                <groupId>xml‐apis</groupId> 
                <artifactId>xml‐apis</artifactId> 
                <version>1.4.01</version> 
            </dependency> 
        </dependencies> 
    </dependencyManagement> 
    
    <dependencies> 
        <dependency> 
            <groupId>javax.servlet</groupId> 
            <artifactId>servlet‐api</artifactId> 
            <version>${servlet‐api.version}</version> 
            <scope>provided</scope> 
        </dependency> 
    </dependencies> 

    <build> 
        <plugins> 
            <!‐‐ java编译插件 ‐‐> 
            <plugin> 
                <groupId>org.apache.maven.plugins</groupId> 
                <artifactId>maven‐compiler‐plugin</artifactId> 
                <version>3.2</version> 
                <configuration><source>1.8</source> 
                    <target>1.8</target> 
                    <encoding>UTF‐8</encoding> 
                </configuration> 
            </plugin> 
        </plugins> 
    </build> 

</project>
```

### **3.2.2 health_common** 

创建health_common，子工程，打包方式为jar，存放通用组件，例如工具类、实体类等 

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven‐4.0.0.xsd"> 
    <parent> 
        <artifactId>health_parent</artifactId> 
        <groupId>com.itheima</groupId> 
        <version>1.0‐SNAPSHOT</version> 
    </parent> 
    <modelVersion>4.0.0</modelVersion> 
    <artifactId>health_common</artifactId> 
    <packaging>jar</packaging> 

    <dependencies> 
        <dependency> 
            <groupId>com.github.pagehelper</groupId> 
            <artifactId>pagehelper</artifactId> 
        </dependency> 

        <!‐‐ Mybatis ‐‐> 
        <dependency> 
            <groupId>org.mybatis</groupId> 
            <artifactId>mybatis</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.mybatis</groupId> 
            <artifactId>mybatis‐spring</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>com.github.miemiedev</groupId> 
            <artifactId>mybatis‐paginator</artifactId> 
        </dependency> 

        <!‐‐ MySql ‐‐> 
        <dependency> 
            <groupId>mysql</groupId> 
            <artifactId>mysql‐connector‐java</artifactId> 
        </dependency> 

        <!‐‐ 连接池 ‐‐> 
        <dependency> 
            <groupId>com.alibaba</groupId> 
            <artifactId>druid</artifactId></dependency> 
        <dependency> 
            <groupId>commons‐fileupload</groupId> 
            <artifactId>commons‐fileupload</artifactId> 
        </dependency> 

        <!‐‐ Spring ‐‐> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐context</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐beans</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐web</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐webmvc</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐jdbc</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐aspects</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐jms</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐context‐support</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework</groupId> 
            <artifactId>spring‐test</artifactId></dependency> 

        <!‐‐ dubbo相关 ‐‐> 
        <dependency> 
            <groupId>com.alibaba</groupId> 
            <artifactId>dubbo</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.zookeeper</groupId> 
            <artifactId>zookeeper</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>com.github.sgroschupf</groupId> 
            <artifactId>zkclient</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>junit</groupId> 
            <artifactId>junit</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>com.alibaba</groupId> 
            <artifactId>fastjson</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>javassist</groupId> 
            <artifactId>javassist</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>commons‐codec</groupId> 
            <artifactId>commons‐codec</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.poi</groupId> 
            <artifactId>poi</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>redis.clients</groupId> 
            <artifactId>jedis</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>com.qiniu</groupId> 
            <artifactId>qiniu‐java‐sdk</artifactId></dependency> 
        <dependency> 
            <groupId>com.sun.jersey</groupId> 
            <artifactId>jersey‐client</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.apache.poi</groupId> 
            <artifactId>poi‐ooxml</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework.security</groupId> 
            <artifactId>spring‐security‐web</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework.security</groupId> 
            <artifactId>spring‐security‐config</artifactId> 
        </dependency> 
        <dependency> 
            <groupId>org.springframework.security</groupId> 
            <artifactId>spring‐security‐taglibs</artifactId> 
        </dependency> 
    </dependencies> 
</project>
```

### **3.2.3 health_interface** 

创建health_interface，子工程，打包方式为jar，存放服务接口 

**pom.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven‐ 
                             4.0.0.xsd"> 
    <parent> 
        <artifactId>health_parent</artifactId> 
        <groupId>com.itheima</groupId> 
        <version>1.0‐SNAPSHOT</version> 
    </parent> 
    <modelVersion>4.0.0</modelVersion> 
    <artifactId>health_interface</artifactId> 
    <packaging>jar</packaging> 

    <dependencies> 
        <dependency> 
            <groupId>com.itheima</groupId> 
            <artifactId>health_common</artifactId> 
            <version>1.0‐SNAPSHOT</version> 
        </dependency> 
    </dependencies> 

</project>
```

### **2.2.4 health_service_provider** 

创建health_service_provider，子工程，打包方式为war，作为服务单独部署，存放服务 

类、Dao接口和Mapper映射文件等 

#### **1. log4j.properties**

#### **2. pom.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven‐4.0.0.xsd"> 
    <parent> 
        <groupId>com.itheima</groupId> 
        <artifactId>health_parent</artifactId> 
        <version>1.0‐SNAPSHOT</version> 
    </parent> 
    <modelVersion>4.0.0</modelVersion> 
    <artifactId>health_service_provider</artifactId> 
    <packaging>war</packaging> 

    <dependencies> 
        <dependency> 
            <groupId>com.itheima</groupId> 
            <artifactId>health_interface</artifactId> 
            <version>1.0‐SNAPSHOT</version> 
        </dependency> 
    </dependencies> 

    <build> 
        <plugins> 
            <plugin>
                <groupId>org.apache.tomcat.maven</groupId> 
                <artifactId>tomcat7‐maven‐plugin</artifactId> 
                <configuration> 
                    <!‐‐ 指定端口 ‐‐> 
                    <port>81</port> 
                    <!‐‐ 请求路径 ‐‐> 
                    <path>/</path> 
                </configuration> 
            </plugin> 
        </plugins> 
    </build> 

</project> 
```

#### **3. SqlMapConfig.xml** 

```xml
<?xml version="1.0" encoding="UTF‐8" ?> 
<!DOCTYPE configuration PUBLIC "‐//mybatis.org//DTD Config 3.0//EN" 
"http://mybatis.org/dtd/mybatis‐3‐config.dtd"> 
<configuration> 

    <plugins> 
        <!‐‐ com.github.pagehelper 为 PageHelper 类所在包名 ‐‐> 
        <plugin interceptor="com.github.pagehelper.PageHelper"> 
            <!‐‐ 设置数据库类型 
            Oracle,Mysql,MariaDB,SQLite,Hsqldb,PostgreSQL 六种数据库‐‐> 
            <property name="dialect" value="mysql"/> 
        </plugin> 
    </plugins> 

</configuration>
```

#### 4. spring-dao.xml

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:context="http://www.springframework.org/schema/context" 
       xmlns:p="http://www.springframework.org/schema/p" 
       xmlns:aop="http://www.springframework.org/schema/aop" 
       xmlns:tx="http://www.springframework.org/schema/tx" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring‐beans‐4.2.xsd 
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring‐context.xsd 
                           http://www.springframework.org/schema/aop 
                           http://www.springframework.org/schema/aop/spring‐aop.xsd 
                           http://www.springframework.org/schema/tx 
                           http://www.springframework.org/schema/tx/spring‐tx.xsd 
                           http://www.springframework.org/schema/util 
                           http://www.springframework.org/schema/util/spring‐util.xsd"> 

    <!‐‐数据源‐‐> 
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" destroy‐ method="close"> 
        <property name="username" value="root" /> 
        <property name="password" value="root" /> 
        <property name="driverClassName" value="com.mysql.jdbc.Driver" /> 
        <property name="url" value="jdbc:mysql://localhost:3306/health" /> 
    </bean> 

    <!‐‐spring和mybatis整合的工厂bean‐‐> 
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean"> 
        <property name="dataSource" ref="dataSource" /> 
        <property name="configLocation" value="classpath:SqlMapConfig.xml" /> 
    </bean> 

    <!‐‐批量扫描接口生成代理对象‐‐><!‐‐批量扫描接口生成代理对象‐‐> 
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer"> 
        <!‐‐指定接口所在的包‐‐> 
        <property name="basePackage" value="com.itheima.dao" /> 
    </bean> 

</beans>
```

#### **5. spring-tx.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
       xmlns:context="http://www.springframework.org/schema/context" 
       xmlns:tx="http://www.springframework.org/schema/tx" 
       xmlns:mvc="http://www.springframework.org/schema/mvc" 
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring‐beans.xsd 
                           http://www.springframework.org/schema/mvc 
                           http://www.springframework.org/schema/mvc/spring‐mvc.xsd 
                           http://www.springframework.org/schema/tx 
                           http://www.springframework.org/schema/tx/spring‐tx.xsd 
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring‐context.xsd"> 

    <!‐‐ 事务管理器 ‐‐> 
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"> 
        <property name="dataSource" ref="dataSource"/> 
    </bean> 
    <!‐‐
    	开启事务控制的注解支持
    		注意：此处必须加入proxy‐target‐class="true"， 
   				 需要进行事务控制，会由Spring框架产生代理对象， 
    			 Dubbo需要将Service发布为服务，要求必须使用cglib创建代理对象。
    ‐‐> 
    <tx:annotation‐driven transaction‐manager="transactionManager" proxy‐target‐class="true"/> 

</beans>
```

#### **6. spring-service.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
       xmlns:context="http://www.springframework.org/schema/context" 
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
       xmlns:mvc="http://www.springframework.org/schema/mvc" 
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring‐beans.xsd 
                           http://www.springframework.org/schema/mvc 
                           http://www.springframework.org/schema/mvc/spring‐mvc.xsd 
                           http://code.alibabatech.com/schema/dubbo 
                           http://code.alibabatech.com/schema/dubbo/dubbo.xsd 
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring‐context.xsd"> 

    <!‐‐ 指定应用名称 ‐‐> 
    <dubbo:application name="health_service_provider"/>

    <!‐‐指定暴露服务的端口，如果不指定默认为20880‐‐> 
    <dubbo:protocol name="dubbo" port="20887"/> 

    <!‐‐指定服务注册中心地址‐‐> 
    <dubbo:registry address="zookeeper://127.0.0.1:2181"/> 

    <!‐‐批量扫描，发布服务‐‐>
    <dubbo:annotation package="com.itheima.service"/> 

</beans> 
```

#### **7.web.xml**

```XML
<!DOCTYPE web‐app PUBLIC 
"‐//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" 
"http://java.sun.com/dtd/web‐app_2_3.dtd" > 
<web‐app> 

    <display‐name>Archetype Created Web Application</display‐name> 
    <!‐‐ 加载spring容器 ‐‐> 
    <context‐param> 
        <param‐name>contextConfigLocation</param‐name> 
        <param‐value>classpath*:applicationContext*.xml</param‐value> 
    </context‐param> 
    <listener> 
        <listener‐class>
            org.springframework.web.context.ContextLoaderListener
        </listener‐class> 
    </listener> 

</web‐app> 
```

### **2.2.5 health_backend** 

创建health_backend，子工程，打包方式为war，单独部署，存放Controller、页面等 

#### **1. pom.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<project xmlns="http://maven.apache.org/POM/4.0.0" 
         xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                             http://maven.apache.org/xsd/maven‐4.0.0.xsd"> 
    <parent> 
        <artifactId>health_parent</artifactId> 
        <groupId>com.itheima</groupId> 
        <version>1.0‐SNAPSHOT</version> 
    </parent> 
    <modelVersion>4.0.0</modelVersion> 
    <artifactId>health_backend</artifactId> 
    <packaging>war</packaging> 

    <dependencies> 
        <dependency> 
            <groupId>com.itheima</groupId> 
            <artifactId>health_interface</artifactId> 
            <version>1.0‐SNAPSHOT</version> 
        </dependency> 
    </dependencies> 

    <build> 
        <plugins> 
            <plugin> 
                <groupId>org.apache.tomcat.maven</groupId> 
                <artifactId>tomcat7‐maven‐plugin</artifactId> 
                <configuration> 
                    <!‐‐ 指定端口 ‐‐> 
                    <port>82</port> 
                    <!‐‐ 请求路径 ‐‐> 
                    <path>/</path> 
                </configuration> 
            </plugin> 
        </plugins> 
    </build> 

</project>
```

#### **2. log4j.properties**

#### **3. springmvc.xml**

```xml
<?xml version="1.0" encoding="UTF‐8"?> 
<beans xmlns="http://www.springframework.org/schema/beans" 
       xmlns:xsi="http://www.w3.org/2001/XMLSchema‐instance" 
       xmlns:context="http://www.springframework.org/schema/context" 
       xmlns:dubbo="http://code.alibabatech.com/schema/dubbo" 
       xmlns:mvc="http://www.springframework.org/schema/mvc" 
       xsi:schemaLocation="http://www.springframework.org/schema/beans 
                           http://www.springframework.org/schema/beans/spring‐beans.xsd 
                           http://www.springframework.org/schema/mvc 
                           http://www.springframework.org/schema/mvc/spring‐mvc.xsd 
                           http://code.alibabatech.com/schema/dubbo 
                           http://code.alibabatech.com/schema/dubbo/dubbo.xsd 
                           http://www.springframework.org/schema/context 
                           http://www.springframework.org/schema/context/spring‐context.xsd"> 

    <mvc:annotation‐driven> 
        <mvc:message‐converters register‐defaults="true"> 
            <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter"> 
                <property name="supportedMediaTypes" value="application/json"/> 
                <property name="features"> 
                    <list> 
                        <value>WriteMapNullValue</value> 
                        <value>WriteDateUseDateFormat</value> 
                    </list> 
                </property> 
            </bean> 
            </mvc:message‐converters> 
        </mvc:annotation‐driven> 

    <!‐‐ 指定应用名称 ‐‐> 
    <dubbo:application name="health_backend" /> 

    <!‐‐指定服务注册中心地址‐‐> 
    <dubbo:registry address="zookeeper://127.0.0.1:2181"/> 

    <!‐‐批量扫描‐‐> 
    <dubbo:annotation package="com.itheima.controller" /> 

    <!‐‐
    超时全局设置 10分钟 
    check=false 不检查服务提供方，开发阶段建议设置为falsecheck=false 不检查服务提供方，开发阶段建议设置为false 
    check=true 启动时检查服务提供方，如果服务提供方没有启动则报错 
    ‐‐> 
    <dubbo:consumer timeout="600000" check="false"/> 

    <!‐‐文件上传组件‐‐> 
    <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver "> 
        <property name="maxUploadSize" value="104857600" /> 
        <property name="maxInMemorySize" value="4096" /> 
        <property name="defaultEncoding" value="UTF‐8"/> 
    </bean> 

</beans>
```

#### **4 .web.xml**

```xml
<!DOCTYPE web‐app PUBLIC 
"‐//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" 
"http://java.sun.com/dtd/web‐app_2_3.dtd"> 
<web‐app> 
    <display‐name>Archetype Created Web Application</display‐name> 
    
    <!‐‐ 解决post乱码 ‐‐> 
    <filter> 
        <filter‐name>CharacterEncodingFilter</filter‐name> 
        <filter‐class>org.springframework.web.filter.CharacterEncodingFilter</filter‐class>
        <init‐param> 
            <param‐name>encoding</param‐name> 
            <param‐value>utf‐8</param‐value> 
        </init‐param> 
        <init‐param> 
            <param‐name>forceEncoding</param‐name> 
            <param‐value>true</param‐value> 
        </init‐param> 
    </filter> 
    <filter‐mapping> 
        <filter‐name>CharacterEncodingFilter</filter‐name> 
        <url‐pattern>/*</url‐pattern> 
        </filter‐mapping> 
    <servlet> 
        <servlet‐name>springmvc</servlet‐name> 
        <servlet‐class>org.springframework.web.servlet.DispatcherServlet</servlet‐class> 
        
        <!‐‐ 指定加载的配置文件 ，通过参数contextConfigLocation加载 ‐‐> 
        <init‐param> 
            <param‐name>contextConfigLocation</param‐name> 
            <param‐value>classpath:springmvc.xml</param‐value> 
        </init‐param> 
        <load‐on‐startup>1</load‐on‐startup> 
    </servlet> 
    <servlet‐mapping> 
        <servlet‐name>springmvc</servlet‐name> 
        <url‐pattern>*.do</url‐pattern> 
        </servlet‐mapping> 
</web‐app>
```


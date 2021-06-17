[toc]
# Kafka

## 了解Kafka

### Kafka简介

Kafka是由**Apache软件基金会**开发的一个开源流处理平台，由Scala和Java编写

Kafka是一种**高吞吐量的分布式发布订阅消息系统**，它可以处理消费者在网站中的所有动作流数据。 这种动作（网页浏览，搜索和其他用户的行动）是在现代网络上的许多社会功能的一个关键因素。 这些数据通常是由于吞吐量的要求而通过处理日志和日志聚合来解决。 对于像Hadoop一样的日志数据和离线分析系统，但又要求实时处理的限制，这是一个可行的解决方案

Kafka的目的是通过Hadoop的并行加载机制来统一线上和离线的消息处理，也是为了**通过集群来提供实时的消息**

### 作者

<img src="https://media.licdn.cn/dms/image/C4D03AQG4S579QRyXEQ/profile-displayphoto-shrink_200_200/0/1556772118690?e=1627516800&v=beta&t=iK_7seuEUCzn06ObaetHk3kLNS8xJhWkclE43CiIY-g" alt="img" style="zoom: 67%;" />

Jay Kreps是 LinkedIn的一名在线数据架构技术高管，其负责LinkedIn开源项目，包括Apache Kafka、Apache Samza、Voldemort以及Azkaban等项目

Kafka的架构师Jay Kreps对于Kafka的名称由来是这样讲的，由于Jay Kreps非常喜欢Franz Kafka,并且觉得Kafka这个名字很酷，因此取了个和消息传递系统完全不相干的名称Kafka，该名字并没有特别的含义

Kafka的诞生，是为了解决LinkedIn的数据管道问题，起初LinkedIn采用了ActiveMQ来进行**数据交换**，大约是在2010年前后，那时的ActiveMQ还远远无法满足LinkedIn对数据传递系统的要求，经常由于各种缺陷而导致消息阻塞或者服务无法正常访问，为了能够解决这个问题，LinkedIn决定研发自己的消息传递系统，当时LinkedIn的首席架构师Jay Kreps便开始组织团队进行消息传递系统的研发



## 发布订阅模式  / 观察者模式

####  观察者模式

![image-20210527183752795](http://qiniu.mengxun.online/image-20210527183752795.png)

#### 发布订阅模式

![image-20210527183903003](http://qiniu.mengxun.online/image-20210527183903003.png)

#### 总结

![image-20210527184047716](http://qiniu.mengxun.online/image-20210527184047716.png)



## 实践

### 项目简介

![项目简介图](http://qiniu.mengxun.online/20210523115536.png)

### 辛酸的跑模型过程

![1621854969069](http://qiniu.mengxun.online/1621854969069.png)

![1621856937838](http://qiniu.mengxun.online/1621856937838.png)

![1621857379753](http://qiniu.mengxun.online/1621857379753.png)

### 遇到问题

每次跑模型都会花费大约半个小时的时间，所以只能跑一次模型，不能每个请求过来都跑一次模型，所以需要利用这一个跑以来的模型不停的获取输入数据

### 解决思路：Kafka

#### Kafka  demo

##### 创建主题

```sh
bin/Kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 
--partitions 1 --topic test
```

##### 创建消费者

```sh
bin/Kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic test --from-begin
```

##### 创建生产者

```sh
bin/Kafka-console-producer.sh --broker-list  localhost:9092 --topic test
```



![1621857260448](http://qiniu.mengxun.online/1621857260448.png)

![1621857241805](http://qiniu.mengxun.online/1621857241805.png)



#### 结合python实现功能

![1621872505860](http://qiniu.mengxun.online/1621872505860.png)

![1621871850253](http://qiniu.mengxun.online/1621871850253.png)

![1621871941272](http://qiniu.mengxun.online/1621871941272.png)

这样就可以通过启动一次模型，结合消息队列的模式，实现处理多个请求的功能啦

但是这样还不行，因为我得将数据返回给用户，所以重新定义传入的数据结构

```json
${uuid(n位)}${string}
```

![1621942739220](http://qiniu.mengxun.online/1621942739220.png)

##### 生产者消息队列

![1621943124654](http://qiniu.mengxun.online/1621943124654.png)

##### 插入数据库成功

![1621943220132](http://qiniu.mengxun.online/1621943220132.png)

##### 数据库记录

![1621956746476](http://qiniu.mengxun.online/1621956746476.png)

##### 更改数据结构

```python
{[key:value]}
{"uuid":"28eedc88-7bc0-42dd-8c13-91a6e2bc7754","nature":"Can you tell me how much this is?","database":"cinema"}
```

![1621959841339](http://qiniu.mengxun.online/1621959841339.png)

![1621959882642](http://qiniu.mengxun.online/1621959882642.png)

my_eval.py

![1621959945535](http://qiniu.mengxun.online/1621959945535.png)

数据库

![1621960009302](http://qiniu.mengxun.online/1621960009302.png)

#### 思考

问题一：后台JavaSpringBoot项目中对my_eval.py文件处理用户请求的时间未知，后期可能要考虑python中线程的方式使模型处理每个请求不是按顺序等待的

问题二：一个模型处理所有的请求效率太慢，或者出错后模型运行终止，导致服务瘫痪，后期考虑使用分布式，部署多个模型，提升效率

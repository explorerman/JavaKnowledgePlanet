## 缓存的通用性

缓存命中

缓存一致性问题

缓存应用地方：all

缓存存储方式：hash存储，顺序存储

解决hash存储问题：一致性哈希

## memcached

使用场景：

1. 缓存业务数据：表单数据，字典项等基本，所有的都放缓存了

## redis

使用场景：

1. 主要用来存字典表
2. 也可以存业务数据
3. 存基础平台的一些信息：机构树，人员数等

![image-20200511171919456](C:\Users\JIEE_Shine\AppData\Roaming\Typora\typora-user-images\image-20200511171919456.png)

![image-20200511171854077](C:\Users\JIEE_Shine\AppData\Roaming\Typora\typora-user-images\image-20200511171854077.png)

redis的目标就是为了防止阻塞

redis为了防止出现bigkey，hotkey

为了防止bigkey，将redis拆分，把一个大json放到一个对象里，会导致bigkey问题，把对象拆分，类似于list记录id,每个id对应json的子数据。

[redis中hotkey,bigkey的解决方法](https://www.jianshu.com/p/58615a1e2cac)

[本地缓存等](https://www.jianshu.com/p/c50c3f22650b)

复习内容：redis的基础结构，zset（跳表）的数据结构，LRU，LFU,持久化，其他的一些实现：布隆过滤器，hyperloglog等，分布式锁。

## redis使用

redis，jedis,spring-data-redis,redisson这四者的区别

首先redis是Apache的开源项目

后面的都是在redis的基础上衍生出来的框架

jedis:是redis的java实现客户端，提供了比较全面的redis命令的支持

redisson:实现了分布式和可扩展的java数据结构

lettuce:高级redis客户端，用于线程安全同步，异步和响应使用，支持集群，sentinel,管道和编译器

优点：

Jedis：比较全面的提供了Redis的操作特性

Redisson：促使使用者对Redis的关注分离，提供很多分布式相关操作服务，例如，分布式锁，分布式集合，可通过Redis支持延迟队列

Lettuce：主要在一些分布式缓存框架上使用比较多

可伸缩：

Jedis：使用阻塞的I/O，且其方法调用都是同步的，程序流需要等到sockets处理完I/O才能执行，不支持异步。Jedis客户端实例不是线程安全的，所以需要通过连接池来使用Jedis。

Redisson：基于Netty框架的事件驱动的通信层，其方法调用是异步的。Redisson的API是线程安全的，所以可以操作单个Redisson连接来完成各种操作

Lettuce：基于Netty框架的事件驱动的通信层，其方法调用是异步的。Lettuce的API是线程安全的，所以可以操作单个Lettuce连接来完成各种操作

结论：

建议使用：Jedis + Redisson

[参考链接](https://www.jianshu.com/p/9c798d263037)

[参考链接2](https://www.cnblogs.com/williamjie/p/11287292.html)

## es

简单介绍一下es，这边的业务场景是存列表，存表单数据。原理就是倒排索引，准备一下倒排索引的概念。
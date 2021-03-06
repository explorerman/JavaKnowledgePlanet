## mysql

怎么设计一个关系型数据库

hash索引的缺点：

仅能满足：= in，不能使用范围查询

无法被用来避免数据的排序操作

不能利用部分索引键查询

不能避免表扫描

遇到大量hash值相等的情况后性能并一定就会比Btree索引高

bitmap索引

密集索引和稀疏索引

innodb数据即索引，索引即数据

mysiam数据和索引分开

如何定位并优化慢查询sql

1. 根据慢日志定位慢查询sql
2. 使用explain等工具分析sql
3. 修改sql或者尽量让sql走索引

show variable like '%quer%' 查询系统相关变量 slow_query_log 

show status like '%slow_queries%'

set global slow_query_log = on 打开慢查询

set global long_query_time = 1 这是慢查询时间为1s

explain中type（是否走索引，all，ref等），extra（using filesort，using temporary）重点,extra出现以下2项意味着MySQL根本不能使用索引，效率会受到重大影响，应尽可能对此进行优化

强制走索引：force index 

联合索引的最左匹配原则：

myisam与innodb关于锁方面的区别：

rc,rr级别下的innodb的非阻塞读如何实现，rr:repeatable read;  rc:read committed

给select上排他锁，可以在后面加for update

innodb是采用二段锁，加锁和减锁是分两个步骤进行的

show variable like 'autocommit'

set auto commit = 0; 关闭自动提交

innodb加共享锁：lock in share mode

表级锁跟索引无关，涉及到表里的数据都会上表锁。

**行级锁，除了主键索引外，别的键二级索引，只要使用索引，涉及的行都会被上共享锁或者排他锁**

当不走索引的时候，整张表都会被锁住，用的是表级锁，所以innodb在sql没用到索引的时候，会走表锁。

走索引用的是行级锁，或gap锁（走普通非唯索引的时候用到）

innodb还支持表级意向锁，ls，lx

myisam适合的场景：

1. 频繁执行全表count语句
2. 对数据进行增删改的频率不高，查询非常频繁。
3. 没有事务

innodb适合的场景：

1. 数据增删查改都相当频繁
2. 可靠性要求比较高，要求支持事务

select @@tx_isolation查看数据库的隔离级别

set session  transaction isolation levle read uncommitted 设置为读未提交

rr级别下：当前读都是最新的，也就是符合可重复读的隔离级别

快照读，可能读到之前的记录，也可能读到最新的记录

undo包含：insert undo,update undo

insert undo 在事务提交后，就没用了

update undo重要，更新，删除都靠它

read view针对可重复读

3-16重新看

## redis

Redis集群

如何从海量数据里快速找到所需？

分片：按某种规则去划分数据，分散存储在多个节点上

一致性哈希：根据ip或者业务id，hash取模组成hash环

缺点：hash环的数据倾斜问题，可以引入虚拟节点。

Redis主从同步：全同步过程

增量同步过程

Redis sentinel哨兵

解决主从同步maser宕机后的主从切换问题：

- 监控：检查主从服务器是否运行正常
- 提醒：通过API向管理员或者其他应用程序发送故障通知
- 自动故障迁移：主从切换

流言协议gossip：

在杂乱无章中寻找一致

- 每个节点都随机地与对方通信，最终所有节点的状态达成一致
- 种子节点定期随机向其他节点发送节点列表以及需要传播的消息
- 不保证信息一定会传递给所有节点，但是最终会趋于一致

AOF持久化：

日志重写解决AOF文件大小不断增大的问题：fork等

rdb的配置文件

save(阻塞Redis的服务进程，直到rdb文件被创建完毕),bgsave，可以使用Java定时器去调用bgsave，按照时间存放不同的dump.rdb文件

自动触发rdb持久化

- 根据Redis.config配置里的save m n定时触发（用的是bgsave）
- 主从复制时，主节点自动触发
- 执行debug reload
- 执行shutdown且没有开启aof持久化

bgsave的原理：fork，copy-on-write

Redis的pub/sub是实时的，如果当时没收到，那就收不到了，要想实现可控制的，使用kafka等专业消息队列

分布式锁：

需要解决的问题

- 互斥性
- 安全性
- 死锁
- 容错

setnx key value：如果key不存在，则创建并赋值

时间复杂度是1，设置成功返回1，失败0

如何解决setnx长时间存在的问题，使用expire设置过期时间。

缺点：原子性不能得到保障。

set key value [EX seconds] [PX milliseconds] [NX] [XX]

EX second:设置键的过期时间为second秒

PX millisecond:设置键的过期时间为millisecond毫秒

NX：只在键不存在时，才对键进行设置操作

XX：只在键已经存在时，才对键进行设置操作。

set操作成功完成时，返回OK，否则返回null

mem和Redis的区别：

mem:代码层次类似hash，mem不支持主从，也不支持分片

Redis：100000+qps

redis快的原因：

- 完全基于内存，绝大部分请求是纯粹的内存操作，执行效率高
- 数据结构简单，对数据操作也简单
- 采用单线程，单线程也能处理高并发请求，想多核可以启动多个实例
- Redis的主线程是单线程的，主线程进行：io处理，以及io对应的相关请求的业务处理，过期键的处理，父子协调，集群协调等，除了io事件之外的逻辑，会被分装成任务，周期性的处理，因为采用单线程的处理，所以多个客户端对同一个键进行写操作的时候，都有主线程串行的操作，省去了上下文切换和锁竞争，也不会有并发的问题。CPU并不是制约Redis并发量的瓶颈，制约瓶颈的是网络。
- 单线程只是在处理网络请求时是单线程的，在处理别的时候，并不是真正的单线程，譬如持久化时的fork
- 使用多路复用io，非阻塞io

多路复用模型：

fd:file descriptor文件描述符 

​	一个打开的文件通过唯一的描述符进行引用，该描述符是打开文件的元数据到文件本身的映射。

Redis采用的多路复用函数：epoll/kqueue/evport/select?

- 因地制宜
- 优先选择时间复杂度为O(1)的IO多路复用函数作为底层实现
- 以时间复杂度为O（n）的select作为保底
- 基于reactor设计模式监听IO事件

Redis的所有命令都是原子的

string: 最基本的数据类型，二进制安全，底层sdshdr，set,get

hash：string元素组成的字典，适合用于存储对象。 hset,hget

List：列表，按照string元素插入顺序排序, lpush, lrange

set：string元素组成的无序集合，通过hash表实现，不允许重复,sadd, smembers

zset:通过分数来为集合中的成员进行从小到大的排序 zadd myzset 3 abc; zadd myzset 1 abd,

zrangebyscore myzset 0 10,取0-10条记录

用于计数的hyperloglog，用于支持存储地理位置信息的geo

底层数据类型：

1. 简单动态字符串
2. 链表
3. 字典
4. 跳表
5. 整数集合
6. 压缩列表
7. 对象

## java

首轮：面试技术基本功，优秀的员工

次轮：架构设计，通过技术解决某些场景下的问题 ，team leading

末轮：稳定性以及未来规划，压工资，部门领导，沟通能力等

认真研究要去的岗位，公司
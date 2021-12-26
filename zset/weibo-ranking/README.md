# Spring Boot + Redis 实现微博热搜排行榜功能

## 业务需求
![image-20211226204845509](http://img.cdn.kuaidiba.cn/md/2021-12-26-124845.png)

实现微博热门事件的按照以下维度的热搜排行榜:
>* 小时榜(当前小时)
>* 日榜(当前时间往前倒推 24 小时)
>* 周榜(当前时间往前倒推 7 天)
>* 月榜(当前时间往前倒推 30 天)

## 技术方案

使用 26 个英文字母来代表微博热门事件, 为每个热门事件生成一个随机数来代表热度. 整体来说, 要模拟实现整体功能, 有以下几个功能需要实现:

1. 初始化数据
    1. 初始化一个月的历史数据
2. 热度刷新
    1. 定时刷新所有事件的热度(对每个热门事件增加随机热度)
3. 热搜排行
    1. 定时统计每天/周/月的热度排行榜
4. 实现小时/日/周/月榜单的查询接口

### 初始化数据

初始化所有的热门事件在 30 天内的每个小时的热度值

### 热度定时刷新

每 10 秒定时增加每个热门事件的热度随机值

### 热搜排行定时刷新

每小时定时统计日/周/月榜

### 榜单查询接口


## 接口文档
Swagger Api 接口文档地址: [接口文档](http://localhost:8080/swagger-ui/index.html)

>* 小时榜(当前小时)
![image-20211226233021817](http://img.cdn.kuaidiba.cn/md/2021-12-26-153021.png)

>* 日榜(当前时间往前倒推 24 小时)
![image-20211226233002052](http://img.cdn.kuaidiba.cn/md/2021-12-26-153002.png)

>* 周榜(当前时间往前倒推 7 天)
![image-20211226233048902](http://img.cdn.kuaidiba.cn/md/2021-12-26-153049.png)

>* 月榜(当前时间往前倒推 30 天)
![image-20211226233145532](http://img.cdn.kuaidiba.cn/md/2021-12-26-153145.png)
# micro-sevice-lanou

使用微服务架构重构单体后台[项目地址](http://dockone.io/article/394)

 ## 特性
 
  [微服务浅析](http://dockone.io/article/394)
  
 ## 安装
   
   - 修改host文件，新增`127.0.0.1 peer1 ` ` 127.0.0.1 peer2` `127.0.0.1 peer3`
   - 首先启动eureka-server高可用服务，使用`peer1` `peer2` `peer3`三个配置启动3个节点
   - 安装rabbitmq消息队列
   - 启动config-server分布式配置中心(依赖rabbitmq的支持)
   
  至此，微服务基础设施启动完成，接下来启动其他服务。
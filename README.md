# Introduction 
Java Common

# Include
Swagger
DataSource
EventType
Utils
Linq

# properties config key

## Swagger配置
swagger.enabled=true
swagger.title=Catalog Api
server.port=8080


## 线程池配置(核心线程数,最大线程数,队列大小,线程最大空闲时间,指定用于新创建的线程名称的前缀)
aub.executor.corePoolSize=10
aub.executor.maxPoolSize=20
aub.executor.queueCapacity=1000
aub.executor.keepAliveSeconds=300
aub.executor.threadNamePrefix=aub-executor-

# dabanshan

## framework

* using lagom(scals) framework as microservice infrastructure.
* using CQRS/ES design pattern, @see https://blog.codecentric.de/en/2017/02/cqrs-event-sourcing-lagom/

## structure

* cookbook-api 菜谱服务
* catalog-api 目录服务
* product-api 商品服务
*
* user-api 用户服务
* cart-api 购物车服务
* warehouse-api 仓库服务
* account-api 结算服务
* recommend-api 推荐服务
* tenant-api 租户(供应商)服务

## how run ?

* use "sbt runAll" run a serviceLocator, a Gateway and embed Cassandra and embed Kafka.
* standalone command - lagomServiceLocatorStart/lagomCassandraStart
* Service locator is running at http://localhost:8000
* Service gateway is running at http://localhost:9000

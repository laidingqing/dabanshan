# dabanshan

## framework

* using lagom(scals) framework as microservice infrastructure.
* using CQRS/ES design pattern, @see https://blog.codecentric.de/en/2017/02/cqrs-event-sourcing-lagom/

## structure

* cookbook-api 菜谱服务
* product-api 商品服务
* user-api 用户服务,包含租户服务
* balance-api 结算服务(购物车, 订单, 对账等)
* recommend-api 推荐服务

## how run ?

* use "sbt runAll" run a serviceLocator, a Gateway and embed Cassandra and embed Kafka.
* standalone command - lagomServiceLocatorStart/lagomCassandraStart
* Service locator is running at http://localhost:8000
* Service gateway is running at http://localhost:9000

# dabanshan

## framework

* using lagom framework by scala.

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
* standalone command - @see lagom website
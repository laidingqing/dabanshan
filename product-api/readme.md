# product-api

> “商品微服务”提供商品相关接口服务

## user story

* 作为运维人员, 可以录入基础分类信息以及分类下属性, 基础规格数据
* 作为供应商用户, 可以录入店内商品基本信息: 品名, 单位, 描述, 价格, 分类
* 作为供应商用户, 可以维护商品信息: 更新基本信息, 添加缩略图, 添加详细描述图, 删除缩略图, 删除详细描述图


## api

| URI                                          | Method           | Description             |
| -------------------------------------------- |:----------------:| -----------------------:|
| /api/products                                | POST             | 创建商品                 |
| /api/products/?creatorId&pageNo&pageSize     | GET              | 查询商品                 |
| /api/products/:productId                     | GET              | 查询某个商品              |
| /api/products/:productId                     | PUT              | 更新某个商品              |
| /api/products/:productId/thumbnails          | POST             | 更新某个商品缩略图         |
| /api/products/:productId/thumbnails/:thumbId | DELETE           | 删除某个商品缩图           |
| /api/products/:productId/details             | POST             | 更新某个商品详情图         |
| /api/products/:productId/details             | DELETE           | 删除某个商品详情图         |
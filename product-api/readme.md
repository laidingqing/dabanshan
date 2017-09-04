# product-api

## models

### catalogs - 分类

### attribute_def - 分类属性定义

### products - 商品

    {
        "sku": "编号", //pk
        "name": "名称",
        "description": "描述",
        "thumbnails": ["", ""],
        "price": "原始价格",
        "unit": "单位',
        "stock": "库存",
        "details": ["", "", ""]
    }

### product_attributes - 商品属性

### spec_def - 规格定义

### product_specs - 商品规格

### catalog_by_product - 分类产品

### product_by_catalog - 产品分类

## user story

* 可以录入基础分类信息, 分类下属性, 基础规格数据
* 能够录入商品信息,包含规则, 属性, 详情, 原始价格

## api

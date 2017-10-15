# user-api

> “账号微服务”提供账号相关接口服务, 包括: 账号注册, 账号查询, 等接口

## user story

* 未注册用户可以注册成为平台
* 注册用户可以申请成为平台食材供应商
* 已申请成功食材供应商可以完善信息: 名称,描述,主营,资质

## api


### Create User

**URL**

`POST http://localhost:9000/api/users`

**Request Headers**

`无`

**Request Body**

```json
{
	"firstName": "lai",
	"lastName": "dingqing",
	"email": "ldq@123.com",
	"username": "laidingqing",
	"password": "test12345"
}
```

**Response Body**

```json
{
  "id": "9bdfe4d3ef23"
}
```

### User Login

**URL**

`POST http://localhost:9000/api/users/login`

**Request Headers**

`无`

**Request Body**

```json
{
	"username": "String",
	"password": "String"
}
```
** Response Body**
```json
{
    "id": "String",
    "username": "String",
	"authToken": "String",
	"refreshToken": "String"
}
```

### User Create Tenant

**URL**

`POST /api/users/:userId/tenants/`

**Request Body**

```json
{
  "name": "String", //名称
  "userId": "String",//用户编号
  "address": "Option[String]",//地址
  "phone": "Option[String]",//电话
  "province": "Option[String]",//省
  "city": "Option[String]",//市
  "county": "Option[String]",//县
  "description": "Option[String]"//描述
}
```


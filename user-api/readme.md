# user-api

> “账号微服务”提供账号相关接口服务, 包括: 账号注册, 账号查询, 等接口

## user story

*

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



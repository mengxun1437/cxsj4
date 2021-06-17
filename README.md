# cxsj4

## 前言

### 使用技术

1. SpringBoot、Mysql、Kafka
2. 主要整合框架   JPA、SwaggerUI、OpenFeign



### 基地址

**baseURL** = "http://api.mengxun.online/cxsj4" ;

微信小程序开发者工具设置中打开不检验合法域名，域名证书开发结束以后再申请



### 请求体

除了 1.1 和 1.2 两个接口，其他所有接口都需要在请求的**Header**中加入**Authorization**参数，参数值为用户的**openid**

支持的请求有：***GET POST PUT DELETE***

### 返回体

| 返回码 code | 返回信息 msg         | 返回数据 data        |
| ----------- | -------------------- | -------------------- |
| 0           | 成功操作后返回的信息 | 成功操作后返回的数据 |
| 40000       | 失败操作返回的信息   | 失败操作后返回的数据 |



## API

### 目录

[toc]

### 文档

#### 调用说明

你可以[**在线测试**](http://api.mengxun.online/cxsj4/swagger-ui.html) ,线上测试入口将会在小程序上线后关闭



#### 1	用户模块 User

##### 1.1	通过Code获取微信OpenId

**接口**

​	***GET	baseURL + "/user/openId?code={code}"***

**提交参数**

无

**返回参数**

​	code ```0```

```json
{
  "code": 0,
  "msg": "获取用户OpenId成功",
  "data": {
      "openid":'${用户openid}'
  }
}
```

​	code ```40000```

```json
{
  "code": 40000,
  "msg": "获取用户OpenId失败",
  "data": {
    "errcode":'${微信获取openid报错码}',
    "errmsg": '${微信获取openid报错信息}'
  }
}
```



##### 1.2	保存用户信息

**接口**

​	***POST	baseURL + "/user/"***

**提交参数**

| 参数     | 类型   | 必要性 | 说明                   |
| -------- | ------ | ------ | ---------------------- |
| openId   | String | 是     | 通过code获取到的openId |
| nickName | String | 是     | 用户的微信昵称         |
| avatar   | String | 是     | 用户头像链接           |

**返回参数**

​	code ```0```

```json
{
  "code": 0,
  "msg": "新增用户成功",
  "data": {
      "openId":'${用户的openId}',
      "created":'${用户创建的日期}',
      "updated":'${用户修改的日期}'
  }
}
```

​	code ```40000```

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



##### 1.3 获取用户信息

**接口**

​	***GET 	baseURL + '/user/{openid}'***

**提交参数**

无

**返回参数**

​	code `0`

```json
{
  "code": 0,
  "msg": "success",
  "data": {
      "openId":'${用户的openId}',
      "nickName":'${用户的昵称}',
      "avatar":'${用户的头像链接}',
      "created":'${用户创建的日期}',
      "updated":'${用户修改的日期}'
  }
}
```

​	code `40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



#### 2 数据库配置模块 DbConfig

##### 2.1 获取所有的驱动列表

**接口**

​	***GET	baseURL + '/driver/'***

**提交参数**

​	无

**返回参数**

​	code `0`

```json
{
  "code": 0,
  "msg": "success",
  "data": [
      ...
      {
      "driverId": '${驱动id}',
      "driverName": '${驱动name,example:com.mysql.jdbc.Driver}',
      "driverType": '${驱动类型,example:mysql}'
    }
      ...
  ]
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



##### 2.2 增加一个数据库配置信息

**接口**

​	***POST	baseURL + '/database/'***

**提交参数**

| 参数       | 类型   | 必要性 | 说明                   |
| ---------- | ------ | ------ | ---------------------- |
| openId     | String | 是     | 通过code获取到的openId |
| dbIp       | String | 是     | 数据库所在主机的ip     |
| dbName     | String | 是     | 需要连接的数据库名称   |
| dbPort     | Number | 是     | 数据库的端口号         |
| dbUser     | String | 是     | 数据库用户名           |
| dbPwd      | String | 是     | 数据库密码             |
| dbDriverId | String | 是     | 数据库密码             |
| shortName  | String | 是     | 前端向用户展示的短名   |

**返回参数**

​	code `0`

```json
{
  "code": 0,
  "msg": "success",
  "data": {
      "dbId":'${数据库id}',
      "openId":'${用户openId}',
      "created":'${创建日期}',
      "updated":'${修改日期}'
  }
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



##### 2.3 获取用户所有的数据库

**接口**

​	***GET	baseURL + '/database/'***

**提交参数**

无

**返回参数**

code `0`

```json
{
  "code": 0,
  "msg": "success",
  "data": [
      ...
      {
      "dbId":'${数据库id}',
      "shortName":'${数据库的简名}',
      "created":'${创建日期}',
      "updated":'${修改日期}'
  	  }
      ...
      ]
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



##### 2.4 通过dbId获取数据库配置的具体信息

**接口**

​	***GET	baseURL + '/database/{dbId}'***

**提交参数**

无

**返回参数**

code `0`

```json
{
  "code": 0,
  "msg": "success",
  "data":{
      "dbId": '${数据库Id}',
      "openId": '${用户openId}',
      "shortName": '${数据库简名}',
      "dbName": '${数据库名}',
      "dbUser": '${数据库用户名}',
      "dbIp": '${数据库主机Ip}',
      "dbPort": '${数据库端口}',
      "dbDriverId": '${数据库驱动Id}',
       "created":'${创建日期}',
      "updated":'${修改日期}'
  }
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



##### 2.5 通过dbId修改数据库配置信息

**接口**

​	***PUT	baseURL + '/database/{dbId}'***

**提交参数**

| 参数       | 类型   | 必要性 | 说明                 |
| ---------- | ------ | ------ | -------------------- |
| dbIp       | String | 是     | 数据库所在主机的ip   |
| dbName     | String | 是     | 需要连接的数据库名称 |
| dbPort     | Number | 是     | 数据库的端口号       |
| dbUser     | String | 是     | 数据库用户名         |
| dbDriverId | String | 是     | 数据库密码           |
| shortName  | String | 是     | 前端向用户展示的短名 |

**返回参数**

code `0`

```json
{
  "code": 0,
  "msg": "success",
  "data":{
      "dbId": '${数据库Id}',
      "openId": '${用户openId}',
      "shortName": '${数据库简名}',
      "dbName": '${数据库名}',
      "dbUser": '${数据库用户名}',
      "dbIp": '${数据库主机Ip}',
      "dbPort": '${数据库端口}',
      "dbDriverId": '${数据库驱动Id}',
      "created":'${创建日期}',
      "updated":'${修改日期}'
  }
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



##### 2.6 修改数据库配置信息的密码

**接口**

​	***PUT	baseURL + '/database/password'***

**提交参数**

| 参数  | 类型   | 必要性 | 说明         |
| ----- | ------ | ------ | ------------ |
| dbId  | String | 是     | 数据库Id     |
| dbPwd | String | 是     | 修改后的密码 |

**返回参数**

code `0`

```json
{
  "code": 0,
  "msg": "修改数据库配置密码成功",
  "data":null
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



##### 2.7 通过dbId删除数据库配置信息

**接口**

​	***DELETE	baseURL + '/database/{dbId}'***

**提交参数**

无

**返回参数**

code `0`

```json
{
  "code": 0,
  "msg": "删除数据库配置信息成功",
  "data":null
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```



#### 3 数据查询模块 Select

##### 3.1 获取查询结果

**接口**

​	***POST	baseURL + '/select/'***

**提交参数**

| 参数  | 类型   | 必要性 | 说明                 |
| ----- | ------ | ------ | -------------------- |
| dbId  | String | 是     | 数据库配置信息Id     |
| srSql | String | 是     | 需要查询的数据库语句 |

**返回参数**

code `0`

```json
{
  "code": 0,
  "msg": "查询成功",
  "data":[
      ...
      {
         '${返回数据}' 
      }
      ...
  ]
}
```

​	code	`40000`

```json
{
  "code": 40000,
  "msg": '${错误信息}',
  "data": null
}
```




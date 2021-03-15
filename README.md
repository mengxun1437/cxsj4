# cxsj4

## 前言

### 使用技术

1. SpringBoot、Mysql
2. 主要整合框架   JPA、SwaggerUI、OpenFeign



### 基地址

**baseURL** = "http://api.mengxun.online/cxsj4" ;

微信小程序开发者工具设置中打开不检验合法域名，域名证书开发结束以后再申请



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

| 参数 | 类型   | 必要性 | 说明                     |
| ---- | ------ | ------ | ------------------------ |
| code | String | 是     | 通过wx.login()获取的code |

**返回参数**

​	code ```0```

```json
{
  "code": 40000,
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
      "openid":'${用户的openid}',
      "created":'${用户创建的日期}',
      "updated":'${用户修改的日期}'
  }
}
```

​	code ```40000```

```json
{
  "code": 40000,
  "msg": "${错误信息}",
  "data": null
}
```


# jpa


<a name="overview"></a>
## 概览
jpa测试


### 版本信息
*版本* : 1.0


### 许可信息
*服务条款* : https://swagger.io/


### URI scheme
*域名* : localhost  
*基础路径* : /


### 标签

* hello页面有关接口类 : Jpa Index Controller
* 用户相关接口类 : Jpa User Controller




<a name="paths"></a>
## 资源

<a name="16167199f77f4e7862ccdaaaa406ca41"></a>
### Hello页面有关接口类
Jpa Index Controller


<a name="indexusingget"></a>
#### hello页面
```
GET /index/hello
```


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[请求响应](#aaa541a890449ce7c1f52558995a1fa2)|


##### 生成

* `\*/*`


<a name="ratelimiterusingget"></a>
#### rateLimiter
```
GET /index/rateLimiter
```


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[请求响应](#aaa541a890449ce7c1f52558995a1fa2)|


##### 生成

* `\*/*`


<a name="welcomeusingget"></a>
#### welcome页面
```
GET /index/welcome
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Query**|**用户姓名**  <br>*必填*|用户姓名|string|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|string|


##### 生成

* `\*/*`


<a name="f57bae76d2b18818a37f0f14c0f17f49"></a>
### 用户相关接口类
Jpa User Controller


<a name="adduserusingpost"></a>
#### 添加用户信息
```
POST /user/add
```


##### 参数

|类型|名称|说明|类型|
|---|---|---|---|
|**Body**|**jpaUserAddReqBean**  <br>*必填*|用户信息|[添加用户对象](#ad1e6f4fa43ecf4cadcf7cf27217cb4e)|


##### 响应

|HTTP代码|说明|类型|
|---|---|---|
|**200**|OK|[请求响应OfJpaUser](#6174efea4cac28671fc7c3ac9d47d294)|


##### 消耗

* `application/json`


##### 生成

* `\*/*`




<a name="definitions"></a>
## 定义

<a name="jpaatlasmodel"></a>
### JpaAtlasModel

|名称|类型|
|---|---|
|**address**  <br>*可选*|string|
|**area**  <br>*可选*|string|
|**areaCode**  <br>*可选*|integer (int32)|
|**city**  <br>*可选*|string|
|**cityCode**  <br>*可选*|integer (int32)|
|**ingLat**  <br>*可选*|string|
|**ip**  <br>*可选*|string|
|**province**  <br>*可选*|string|
|**provinceCode**  <br>*可选*|integer (int32)|
|**streetCode**  <br>*可选*|integer (int32)|
|**streetName**  <br>*可选*|string|


<a name="jpauser"></a>
### JpaUser

|名称|说明|类型|
|---|---|---|
|**createTime**  <br>*可选*|创建时间|string (date-time)|
|**id**  <br>*可选*|用户id|integer (int64)|
|**mobile**  <br>*可选*|手机号|string|
|**modifyTime**  <br>*可选*|修改时间|string (date-time)|
|**name**  <br>*可选*|用户姓名|string|
|**pwd**  <br>*可选*|密码|string|
|**sex**  <br>*可选*|性别|integer (int32)|


<a name="ad1e6f4fa43ecf4cadcf7cf27217cb4e"></a>
### 添加用户对象

|名称|说明|类型|
|---|---|---|
|**mobile**  <br>*必填*|手机号|string|
|**name**  <br>*必填*|用户名|string|
|**pwd**  <br>*必填*|密码|string|
|**sex**  <br>*可选*|性别|integer (int32)|


<a name="aaa541a890449ce7c1f52558995a1fa2"></a>
### 请求响应

|名称|说明|类型|
|---|---|---|
|**atlasModel**  <br>*可选*|访问地址|[JpaAtlasModel](#jpaatlasmodel)|
|**data**  <br>*可选*|响应数据|object|
|**errorCode**  <br>*可选*|响应交易码|string|
|**errorMsg**  <br>*可选*|响应交易|string|
|**success**  <br>*可选*|请求状态|boolean|


<a name="6174efea4cac28671fc7c3ac9d47d294"></a>
### 请求响应OfJpaUser

|名称|说明|类型|
|---|---|---|
|**atlasModel**  <br>*可选*|访问地址|[JpaAtlasModel](#jpaatlasmodel)|
|**data**  <br>*可选*|响应数据|[JpaUser](#jpauser)|
|**errorCode**  <br>*可选*|响应交易码|string|
|**errorMsg**  <br>*可选*|响应交易|string|
|**success**  <br>*可选*|请求状态|boolean|






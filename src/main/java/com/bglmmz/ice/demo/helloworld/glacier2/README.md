此demo演示了客户端如何通过远程Glacier2访问它背后的Ice服务

## Glacier2的作用
* 作为防火墙。把ICE服务隔离到Glacier2之后，ICE服务不用直接暴露到公网。
* 作为路由器。客户端访问ICE服务时，可以通过Glacier2来做路由，有Glacier2来定位处于私域的服务。

## Glacier2的好处
* Glacier2支持所有Ice协议(TCP、SSL，以及UDP)
* Glacier2无需编解码消息内容，就可以对请求和答复消息进行路由，因此非常高效。即Glacier2不需要对应用有特别的了解。
* 单个 Glacier 端口能支持任意数目的服务器
* 服务器不知道 Glacier2 的存在，也无需为了使用 Glacier2 而做出任何修改
* 服务器无需为了进行回调而建立与客户的新连接。换句话说，客户端访问服务时建立的连接，可以是双向连接，服务器对客户的回调会在此已有连接上发送，即使客户端也位于它自己的防火墙之后，简化了客户端的防火墙为了支持服务端回调而所需的管理工作
* 要使用Glacier2，客户常常只需改动配置，无需改动代码

## Glacier2的工作方式
* Glacier2是个独立的服务，并且能够同时访问服务端和客户端的两个网络


## Glacier2的使用

### Glacier2配置
```
# 配置Glacier2名称
Glacier2.InstanceName=DefaultGlacier2

# 配置Glcier2的公网IP，用于客户端访问
Glacier2.Client.Endpoints=tcp -p 4064 -h localhost

# 配置Glcier2的内网IP，用于服务端回调客户端时用。如果无需回调客户端，则不用配置，或留空白。
Glacier2.Server.Endpoints=
```

### 服务端

无需任何配置修改，或者代码修改。

### 客户端

* 使用配置文件。在客户端配置文件中，配置Glacier2
```
# 配置Glcier2的名称，和公网IP
Ice.Default.Router=Glacier/router:tcp -h localhost -p 8000
```

* 在代码中直接配置Glacier2
```
 // 配置Glcier2的名称，和公网IP
 properties.setProperty("Ice.Default.Router", "DefaultGlacier2/router:tcp -p 4064 -h localhost");
```

## DEMO运行步骤：

1. 启动Glacier2
打开一个命令行窗口，执行：
```
glacier2router --Ice.Config=D:\github.com\bglmmz\hello_ice\src\main\java\com\bglmmz\ice\demo\helloworld\glacier2\config.glacier2
```  

2. 启动server
在IDE里直接运行HelloServer的main()

3. 启动client
IDE里直接运行HelloClient的main()，输入任意user id / password，即可连接上Glacier2，然后输入命令调用服务接口。

## 说明
在本demo中，客户端访问服务时，还是需要明确知道服务的endpoint。
IceGrid功能强大，不再赘述。本demo主要演示了:
1. IceGrid作为定位服务（Location service）时的功能
2. 服务端动态创建servant，动态注册到IceGrid
3. 客户端访问动态注册的servant

## IceGrid的使用

### IceGrid配置

参考：Registry.cfg中说明

### 服务端

首先需要知道Locator的endpoint
```
properties.setProperty("Ice.Default.Locator", "DefaultIceGrid/Locator:tcp -p 4061");
```

其次配置adapter，并创建adapter
```
String adapterName = "DefaultTCPAdapter";
String adapterId = "icChannelAdapter";
properties.setProperty(adapterName + ".Endpoints", "tcp -h localhost");
properties.setProperty(adapterName + ".AdapterId", adapterName);
...
com.zeroc.Ice.ObjectAdapter adapter = ic.createObjectAdapter(adapterName);

```
然后创建servant，并绑定到adapter
```
com.zeroc.Ice.Object object = new HelloServiceImpl();
String servantId = "task_1_party_id_1";
adapter.add(object, com.zeroc.Ice.Util.stringToIdentity(servantId));
```

最后，激活adapter
```
adapter.activate();
```

### 客户端

首先需要知道Locator的endpoint
```
properties.setProperty("Ice.Default.Locator", "DefaultIceGrid/Locator:tcp -p 4061");
```

然后得到服务代理
```
//寻址地址模式：servantId@adapterId
HelloServicePrx helloService = HelloServicePrx.checkedCast(ic.stringToProxy("task_1_party_id_1@icChannelAdapter"));
```


## DEMO运行步骤：

本demo演示动态创建servant，adapter，并把servant绑定到adapter上。在客户端访问时，通过配置locator，并通过identityID@adapterID来寻址服务。

1. 根据Registry.cfg中的配置
    IceGrid.Registry.LMDB.Path=deploy/lmdb/registry 
    创建目录deploy/lmdb/registry

2. 在命令行窗口，或者IDE的Terminal中运行：
```
icegridregistry.exe --Ice.Config=D:\github.com\bglmmz\hello_ice\src\main\java\com\bglmmz\ice\demo\helloworld\grid\Registry.cfg
```
   
3. 在IDE中运行HelloServer, HelloServer2, 以及HelloClient, HelloClient2，在client中根据提示输入命令即可。

## 说明

1. 可以注册多个servant到同一个adapter, 只要用identityID来区分不同的servant
2. 如果有多个服务进程，多个进程都可以创建相同名称的adapter，但是每个进程中的adapter必须有不同的adapterID
3. 并没有创建IceGrid, IceNode, 仅仅是利用了IceGridRegistry。不要被IceGridRegistry这个名字迷惑，它可以单独作为servant的locator存在。
4. 动态创建（注册）Ice Object（对于我们来说就是servant）需要在iceGridRegistry的配置设置：IceGrid.Registry.DynamicRegistration=1

5. 服务端代码、客户端代码参考源码中注释

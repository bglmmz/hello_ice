本demo演示动态创建servant，adapter，并把servant绑定到adapter上。客户端访问时，无需知道服务端ip和端口。

从代码中可以看出，服务端动态创建servant，adapter和普通的服务端创建国产非常相近。区别主要时是：
1. 代码中创建adapter时，只需要一个名字，而它的endpoints，不需要指定端口。
2. 初始化Communicator时，指定如下参数：properties.setProperty("Ice.Default.Locator", "DefaultIceGrid/Locator:tcp -p 4061 -h localhost"); 创建出的adapter，通过配置项，通知ICE需要以什么ID注册到Registry。
    以及为刚创建的adapter指定如下参数(Endpoints, AdapterId)：
   properties.setProperty("${newAdapterName}.Endpoints", "tcp -h localhost");
   properties.setProperty("${newAdapterName}.AdapterId", taskIceServantAdapterId);
    这样，在激活adapter时，ICE就会把此adapter绑定的servant注册到Registry。

当然，动态注册的前提是，Registry支持动态注册，启动配置Registry.cfg中需要如下参数：
IceGrid.Registry.DynamicRegistration=1

而对客户端来说，要访问Registry中的注册的服务（不管是动态注册的，还是通过配置文件注册的），
首先需要通过Registry来寻找服务地址，寻址形式是：ic.stringToProxy(servantId+"@"+taskIceServantAdapterId)，
当然，初始化Communicator时，需要指定Registry的地址：
properties.setProperty("Ice.Default.Locator", "DefaultIceGrid/Locator:tcp -p 4061");



1. 可以注册多个servant到同一个adapter, 只要用identityID来区分不同的servant
2. 并没有创建IceGrid, IceNode, 仅仅是利用了IceGridRegistry。不要被IceGridRegistry这个名字迷惑，它可以单独作为servant的注册器存在。
3. 动态注册Ice Object（对于我们来说就是servant）需要在iceGridRegistry的配置设置：IceGrid.Registry.DynamicRegistration=1 

** 服务端代码、客户端代码参考源码中注释


demo演示步骤：
1. 根据Registry.cfg中的配置
    IceGrid.Registry.LMDB.Path=deploy/lmdb/registry 
    创建目录deploy/lmdb/registry

2. 在命令行窗口，或者IDEA的Terminal中运行：
   icegridregistry.exe --Ice.Config=D:\github.com\bglmmz\hello_ice\src\main\java\com\bglmmz\ice\demo\helloworld\grid\Registry.cfg
   
3. 在IDEA中运行HelloServer, HelloServer2, 以及HelloClient, HelloClient2，在client中根据提示输入命令即可。


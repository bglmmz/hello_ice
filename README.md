演示
***REMOVED***
1. 普通的ice服务端、客户端 
2. 客户端访问远程Glacier2后的服务
3. 客户端通过IceGridRegistry访问动态注册的服务
4. 客户端访问远程Glacier2后、通过IceGridRegistry动态注册的服务

从例子中可以看出，
1. Glacier2对服务端的编程没有任何影响，对客户端的影响也很小，只需要指定Router地址，以及链接Router即可。
2. 通过Registry寻址服务，模式是：servantId@adapterId，客户端无需知道服务的endpoints。
   服务端编程时，不管是通过配置注册，还是通过代码动态注册，都只需要指定Locator地址, adapter name, adapter Id, 以及servantId即可。
   客户端编程时，只需指定Locator地址，然后用servantId@adapterId来寻址服务代理即可。
3. Glacier2 和 Registry集成时，遵循上面的规则即可。   


有slice生成JAVA源码：
slice2java --output-dir src\main\java slice\hello.ice


com.bglmmz.ice.demo.helloworld.glacier2.client.HelloClient2 扩展自 com.zeroc.Glacier2.Application，这是zeroc提供的客户端的虚拟基类。
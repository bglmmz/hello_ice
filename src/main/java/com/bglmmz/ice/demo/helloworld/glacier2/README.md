此demo演示了客户端如何通过远程Glacier2访问它背后的Ice服务

运行步骤：

1. 打开一个命令行窗口，执行：
   glacier2router --Ice.Config=D:\github.com\bglmmz\hello_ice\src\main\java\com\bglmmz\ice\demo\helloworld\glacier2\config.glacier2
   
2. IDE里直接运行HelloServer的main()

3. IDE里直接运行HelloClient的main()

从代码中可以看出，要部署Glacier2，对服务端来说，什么都不需要改变，只需要增加一个Glacier2的配置，然后就启动Glacier2服务就可以了。
当然，这个Glacier2要能和服务通信，而且远程client也能访问到。

对客户端来说，要访问远程Glacier2后的服务，只需要一个配置：
    properties.setProperty("Ice.Default.Router", "DefaultGlacier2/router:tcp -p 4064 -h 192.168.10.149");
其它任何代码不需要改变。这个参数，就表示所有的调用，都是通过把Glacier2作为Router来进行的。


HelloClient2.java 扩展自 com.zeroc.Glacier2.Application
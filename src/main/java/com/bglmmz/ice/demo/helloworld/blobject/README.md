本demo通过更底层的ObjectPrx来发送调用请求到服务，并从响应中读取数据。

一般而已，我们在客户端得到ObjectPrx后，我们会cast成目标服务代理对象，如：

```
com.zeroc.Ice.ObjectPrx obj = ic.stringToProxy("HelloService:default -p 10002");
HelloServicePrx helloService = HelloServicePrx.checkedCast(obj);
```

然后，在helloService上调用服务。如果我们直接通过obj对象，该如何调用服务呢？请参考HelloClient中的注释说明。

demo中的服务，输入、输出都是简单类型，如果输入输入是复杂类型，序列化/反序列化请参考：
https://github.com/zeroc-ice/ice-demos/tree/3.7/java/Ice/invoke

这种方式有什么用呢？如果我们想自己实现一个dispatcher，有一个统一的服务来接收所有客户端请求，然后把客户端请求分发到具体服务时，就或许有用。




```
java -jar build/libs/server.jar
```
或者在ide里直接run HelloServer


In a separate window, start the client:

```
java -jar build/libs/client.jar
```
或者在ide里直接run HelloClient，然后根据提示输入命令调用服务。


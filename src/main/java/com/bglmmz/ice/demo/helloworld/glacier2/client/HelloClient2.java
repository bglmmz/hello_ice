package com.bglmmz.ice.demo.helloworld.glacier2.client;

import com.bglmmz.ice.demo.helloworld.slice.HelloServicePrx;
import com.zeroc.Glacier2.SessionPrx;
import com.zeroc.Ice.Properties;

public class HelloClient2 extends com.zeroc.Glacier2.Application {

    //应用入口
    public static void main(String[] args) throws RestartSessionException {
        HelloClient2 helloClient2 = new HelloClient2();
        //Glacier2.Application应用入口
        int status = helloClient2.main("HellClient2", args, getInitializationData());
        System.exit(status);
    }

    private static com.zeroc.Ice.InitializationData getInitializationData() {
        com.zeroc.Ice.InitializationData initData = new com.zeroc.Ice.InitializationData();
        Properties properties = com.zeroc.Ice.Util.createProperties();
        //1. 配置默认router
        properties.setProperty("Ice.Default.Router", "DefaultGlacier2/router:tcp -p 4064 -h 192.168.10.149");
        //2. 关闭ACM
        properties.setProperty("Ice.ACM.Client.Close", "0");
        properties.setProperty("Ice.RetryIntervals", "1"); //1毫秒重试1次（只试1次，如果有多个delay值，则试多次, 如0 10 100表示失败后马上重试，再失败delay毫秒再试...)

        initData.properties = properties;
        return initData;
    }

    @Override
    public SessionPrx createSession() {
        //3. 创建Session，Glacier2.Application框架调用
        try {
            com.zeroc.Glacier2.RouterPrx router = com.zeroc.Glacier2.RouterPrx.checkedCast(communicator().getDefaultRouter());
            return router.createSession("foo", "bar");
        } catch (com.zeroc.Glacier2.PermissionDeniedException ex) {
            System.out.println("permission denied:\n" + ex.reason);
        } catch (com.zeroc.Glacier2.CannotCreateSessionException ex) {
            System.out.println("cannot create session:\n" + ex.reason);
        }
        return null;
    }

    @Override
    public int runWithSession(String[] args) throws RestartSessionException {
        //应用的主要处理逻辑，Glacier2.Application框架调用

        //stringToProxy("HelloService:default -p 10001")是直接获取代理，这个"HelloService"是在服务端定义的服务的identityID
        //propertyToProxy("HelloService.Proxy")是用properties中的定义来获取代理。在properties文件的"HelloService.Proxy"这个配置项，配置了在服务端定义的服务的identityID。
        //在properties文件的"HelloService.Proxy"这个配置项的值，就是stringToProxy("HelloService:default -p 10001")这个的参数。

        //"HelloService:tcp -h 192.168.21.26 -p 10001"表明服务的identityId和所在地址端口，这个地址端口可以是远程网络的内网地址端口，远程网络的Glacier2会帮忙转发的。
        HelloServicePrx helloService = HelloServicePrx.checkedCast(communicator().stringToProxy("HelloService:tcp -h 192.168.21.26 -p 10001"));
        if (helloService == null) {
            System.err.println("invalid proxy");
            return 1;
        }

        menu();

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        String line = null;
        do {
            try {
                System.out.print("==> ");
                System.out.flush();
                line = in.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("t ")) {
                    String[] cmd = line.split(" ");
                    if (cmd.length == 2) {
                        String echo = helloService.sayHello(cmd[1]);
                        System.out.println("echo from server:" + echo);
                    } else {
                        System.out.println("unknown command `" + line + "'");
                        menu();
                    }

                } else if (line.equals("s")) {
                    helloService.shutdown();
                } else if (line.equals("x")) {
                    // Nothing to do
                } else if (line.equals("?")) {
                    menu();
                } else {
                    System.out.println("unknown command `" + line + "'");
                    menu();
                }
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            } catch (com.zeroc.Ice.LocalException ex) {
                ex.printStackTrace();
            }
        }
        while (!line.equals("x"));

        return 0;
    }


    private static void menu() {
        System.out.println(
                "usage:\n" +
                        "t xx: send xx to server\n" +
                        "s: shutdown server\n" +
                        "x: exit\n" +
                        "?: help\n");
    }
}

package com.bglmmz.ice.demo.helloworld.blobject.client;

public class HelloClient {
    public static void main(String[] args) {
        int status = 0;

        // Communicator实例
        try (com.zeroc.Ice.Communicator ic = com.zeroc.Ice.Util.initialize(args)) {

            status = run(ic);
        }
        System.exit(status);
    }

    private static int run(com.zeroc.Ice.Communicator ic) {
        //这是常见的、使用HelloServicePrx的方式。这种情况下，需要客户端有HelloServicePrx整个对象。
        //HelloServicePrx helloService = HelloServicePrx.checkedCast(ic.stringToProxy("HelloService:default -p 10002"));


        //这是底层的ObjectPrx对象，此时客户端知道服务端方法名称，但是不能直接通过obj来调用服务端方法，而是要用blobject(blob object)来调用
        com.zeroc.Ice.ObjectPrx obj = ic.stringToProxy("HelloService:default -p 10002");
        if (obj == null) {
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
                    String[] args = line.split(" ");
                    if(args.length==2) {
                        //
                        // Marshal the in parameter.
                        //
                        // 序列化输入参数
                        com.zeroc.Ice.OutputStream out = new com.zeroc.Ice.OutputStream(ic);
                        out.startEncapsulation();
                        out.writeString(args[1]);
                        out.endEncapsulation();


                        //
                        // Invoke operation.
                        //
                        //调用服务端方法
                        com.zeroc.Ice.Object.Ice_invokeResult r = obj.ice_invoke("sayHello", com.zeroc.Ice.OperationMode.Normal, out.finished());
                        if(!r.returnValue) {
                            System.out.println("Unknown user exception");
                            continue;
                        }


                        //
                        // Unmarshal the results.
                        //
                        // 反序列号结果
                        com.zeroc.Ice.InputStream response = new com.zeroc.Ice.InputStream(ic, r.outParams);
                        response.startEncapsulation();

                        //这个readreadString
                        String echo = response.readString();
                        response.endEncapsulation();
                        System.out.println("echo from server:" + echo);
                    }else{
                        System.out.println("unknown command `" + line + "'");
                        menu();
                    }

                } else if (line.equals("s")) {
                    //helloService.shutdown();
                    obj.ice_invoke("shutdown", com.zeroc.Ice.OperationMode.Normal, null);
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

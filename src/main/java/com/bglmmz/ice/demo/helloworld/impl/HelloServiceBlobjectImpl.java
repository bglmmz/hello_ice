package com.bglmmz.ice.demo.helloworld.impl;

import com.zeroc.Ice.Current;
import com.zeroc.Ice.UserException;

public class HelloServiceBlobjectImpl implements com.zeroc.Ice.Blobject{
    @Override
    public Ice_invokeResult ice_invoke(byte[] inEncaps, Current current) throws UserException {
        com.zeroc.Ice.Communicator communicator = current.adapter.getCommunicator();

        com.zeroc.Ice.InputStream in = new com.zeroc.Ice.InputStream(communicator, inEncaps);
        in.startEncapsulation();

        com.zeroc.Ice.Object.Ice_invokeResult r = new com.zeroc.Ice.Object.Ice_invokeResult();
        r.returnValue = true;

        if(current.operation.equals("sayHello")){
            String message = in.readString();
            System.out.println("Server received a greeting: `" + message + "'");

            com.zeroc.Ice.OutputStream out = new com.zeroc.Ice.OutputStream(communicator);
            out.startEncapsulation();
            //写入服务端的响应
            out.writeString("Hello! " + message + ", this is Server.");
            out.endEncapsulation();
            r.outParams = out.finished();
        }else if(current.operation.equals("shutdown")){
            current.adapter.getCommunicator().shutdown();
        }

        //
        // Verify we read all in parameters
        //
        in.endEncapsulation();
        return r;
    }
}

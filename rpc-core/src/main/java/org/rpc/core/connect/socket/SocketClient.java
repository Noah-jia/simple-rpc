package org.rpc.core.connect.socket;

import org.aspectj.lang.annotation.Before;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.connect.entity.RpcRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

//@Component
public class SocketClient implements RequestTransport {

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest, InetSocketAddress inetSocketAddress) {
        return null;
    }
}

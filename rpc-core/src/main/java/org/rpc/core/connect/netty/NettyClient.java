package org.rpc.core.connect.netty;

import org.aspectj.lang.annotation.Before;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.connect.entity.RpcRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class NettyClient implements RequestTransport {
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        return null;
    }
}

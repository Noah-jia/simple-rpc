package org.rpc.core.connect;

import org.rpc.core.connect.entity.RpcRequest;

import java.net.InetSocketAddress;

public interface RequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest, InetSocketAddress inetSocketAddress);
}

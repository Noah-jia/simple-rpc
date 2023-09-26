package org.rpc.core.connect;

import org.rpc.core.connect.entity.RpcRequest;

public interface RequestTransport {
    Object sendRpcRequest(RpcRequest rpcRequest);
}

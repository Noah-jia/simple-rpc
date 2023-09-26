package org.rpc.core.serialize;

import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.connect.entity.RpcResponse;

public interface Serialize {
    byte[] serialize(RpcRequest rpcRequest);
    <T> T deserialize(byte[] bytes,Class<T> clazz);
}

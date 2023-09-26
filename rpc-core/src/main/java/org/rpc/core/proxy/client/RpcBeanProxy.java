package org.rpc.core.proxy.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.config.RpcBeanConfig;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.connect.netty.client.NettyClient;
import org.rpc.core.connect.socket.SocketClient;
import org.rpc.core.utils.ZkUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 加强客户端的Bean，让他进行远程通讯
 */
public class RpcBeanProxy implements InvocationHandler {
    private static final Logger logger= LogManager.getLogger(RpcBeanConfig.class);
    private RpcBeanConfig rpcBeanConfig;

    private RequestTransport requestTransport;

    private ZkUtils zkUtils;
    public RpcBeanProxy(RpcBeanConfig rpcBeanConfig,RequestTransport requestTransport,ZkUtils zkUtils) {
        this.rpcBeanConfig=rpcBeanConfig;
        this.requestTransport=requestTransport;
        this.zkUtils=zkUtils;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.debug("启动代理");
        //建立rpc请求
        RpcRequest rpcRequest = new RpcRequest();
        //生成id，考虑分布式ID？ TODO
        rpcRequest.setRequestId(UUID.randomUUID().toString());
        rpcRequest.setVersion(rpcBeanConfig.getVersion());
        rpcRequest.setInterfaceName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setParameters(args);
        RpcResponse response = new RpcResponse();
        InetSocketAddress inetSocketAddress = zkUtils.loadService(rpcRequest);
        //用netty进行发送
        RpcResponse transport = (RpcResponse)transport(rpcRequest, inetSocketAddress);
        return transport.getData();
    }

    private Object transport(RpcRequest rpcRequest, InetSocketAddress inetSocketAddress) throws InterruptedException, ExecutionException {
        if(requestTransport instanceof NettyClient){
            logger.debug("用netty发送");
            return (RpcResponse)requestTransport.sendRpcRequest(rpcRequest,inetSocketAddress);
        }
        if(requestTransport instanceof SocketClient){
            logger.debug("用socket发送");
            return (RpcResponse)requestTransport.sendRpcRequest(rpcRequest, inetSocketAddress);
        }
        throw new RuntimeException("获取失败");
    }
}

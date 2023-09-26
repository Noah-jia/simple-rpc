package org.rpc.core.proxy.client;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.config.RpcBeanConfig;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.connect.netty.NettyClient;
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
        RpcResponse<Object> response = new RpcResponse<>();
        InetSocketAddress inetSocketAddress = zkUtils.loadService(rpcRequest);
        //用netty进行发送
        Object transport = transport(rpcRequest, inetSocketAddress, response);
        return "hello world";
    }

    private Object transport(RpcRequest rpcRequest, InetSocketAddress inetSocketAddress, RpcResponse<Object> response) throws InterruptedException, ExecutionException {
        if(requestTransport instanceof NettyClient){
            logger.debug("用netty发送");
            CompletableFuture<RpcResponse<Object>> completableFuture=(CompletableFuture<RpcResponse<Object>>) requestTransport.sendRpcRequest(rpcRequest, inetSocketAddress);
            response =completableFuture.get();
        }
        if(requestTransport instanceof SocketClient){
            logger.debug("用socket发送");
            response =(RpcResponse<Object>) requestTransport.sendRpcRequest(rpcRequest, inetSocketAddress);
        }
        if(response.getData()!=null){
            return response.getData();
        }
        throw new RuntimeException("获取失败");
    }
}

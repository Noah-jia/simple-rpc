package org.rpc.core.connect.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.config.RpcBeanConfig;
import org.rpc.core.config.RpcConstants;
import org.rpc.core.connect.entity.RpcMessage;
import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.spring.RpcBeanAware;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ResponseHandler extends SimpleChannelInboundHandler<RpcMessage> {
    private static final Logger logger= LogManager.getLogger(ResponseHandler.class);


    private RpcResponse handlerRequest(RpcRequest rpcRequest) {
        String interfaceName = rpcRequest.getInterfaceName();
        String version = rpcRequest.getVersion();
        RpcBeanConfig beanConfig = (RpcBeanConfig) RpcBeanAware.serviceMap.get(interfaceName + RpcConstants.SERVICE_SPLIT+version);
        Object targetBean = beanConfig.getObject();
        try {
            Method method = targetBean.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            Object result = method.invoke(targetBean, rpcRequest.getParameters());
            RpcResponse rpcResponse = new RpcResponse();
            return rpcResponse.success(result, rpcRequest.getRequestId());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        logger.info("客户端收到的请求为:"+rpcMessage);
        RpcRequest content = (RpcRequest) rpcMessage.getContent();
        RpcResponse response=handlerRequest(content);
        RpcMessage responseMessage = new RpcMessage(rpcMessage.getRequestId(), RpcMessage.RESPONSE, response);
        channelHandlerContext.writeAndFlush(responseMessage);
    }
}

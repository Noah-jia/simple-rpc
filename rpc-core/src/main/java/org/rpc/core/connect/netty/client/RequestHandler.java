package org.rpc.core.connect.netty.client;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.connect.entity.RpcMessage;
import org.rpc.core.connect.entity.RpcResponse;

import org.rpc.core.connect.entity.SimpleFuture;
import org.rpc.core.future.RpcFuture.FutureFactory;
import org.rpc.core.future.RpcFuture.RpcFuture;

public class RequestHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private static final Logger logger= LogManager.getLogger(RequestHandler.class);



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        logger.info("接收到了response");
        RpcResponse content =(RpcResponse) rpcMessage.getContent();
        SimpleFuture simpleFuture = NettyClient.futureMap.get(rpcMessage.getRequestId());
        simpleFuture.setResponse(content);
        simpleFuture.done();
    }
}

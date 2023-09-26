package org.rpc.core.connect.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.connect.entity.RpcMessage;
import org.rpc.core.connect.entity.RpcResponse;

import javax.xml.ws.Response;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private static final Logger logger= LogManager.getLogger(RequestHandler.class);



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage) throws Exception {
        logger.info("接收到了response");
        String id = channelHandlerContext.channel().id().toString();
        LinkedBlockingQueue<RpcResponse> queue = NettyClient.responseMap.get(id);
        RpcResponse content =(RpcResponse) rpcMessage.getContent();
        queue.put(content);
    }
}

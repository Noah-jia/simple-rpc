package org.rpc.core.connect.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.Before;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.connect.entity.RpcMessage;
import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.connect.netty.codec.RpcDecoder;
import org.rpc.core.connect.netty.codec.RpcEncoder;
import org.rpc.core.serialize.Serialize;
import org.rpc.core.serialize.kryo.KryoSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
public class NettyClient implements RequestTransport {

    private static final Logger logger= LogManager.getLogger(NettyClient.class);
    public static final Map<String,LinkedBlockingQueue<RpcResponse>> responseMap=new ConcurrentHashMap<>();
    private Bootstrap bootstrap;
    private NioEventLoopGroup worker;
    private Channel channel;
    private InetSocketAddress inetSocketAddress;



    @PostConstruct
    public void init(){
        this.worker=new NioEventLoopGroup();
        this.bootstrap=new Bootstrap();
        this.bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new RpcDecoder());
                        pipeline.addLast(new RequestHandler());
                    }
                });
    }
    private void start(){
        try {
            ChannelFuture cf = bootstrap.connect(inetSocketAddress).sync().addListener((ChannelFutureListener) channelFuture -> {
                        if(channelFuture.isSuccess()){
                            logger.info("客户端连接成功");
                        }else{
                            logger.info("客户端连接失败");
                        }
            });
            this.channel=cf.channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            worker.shutdownGracefully();
        }
    }
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest, InetSocketAddress inetSocketAddress) {
        String str = inetSocketAddress.getAddress().toString();
        String ip = str.split("/")[1];
        this.inetSocketAddress=new InetSocketAddress(ip, inetSocketAddress.getPort());
        System.out.println(inetSocketAddress.getAddress().toString());
        System.out.println(inetSocketAddress.getPort());
        //链接服务端
        new Thread(this::start).start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LinkedBlockingQueue<RpcResponse> responseQueue = new LinkedBlockingQueue<>();
        responseMap.put(channel.id().toString(),responseQueue);
        RpcMessage rpcMessage = new RpcMessage(1L,0,rpcRequest);
        channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                logger.info("发送rpc请求成功");
            }else{
                logger.error("发送rpc请求失败");
            }
        });
        try {
            return responseQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

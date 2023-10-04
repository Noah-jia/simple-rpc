package org.rpc.core.connect.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultPromise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.connect.entity.RpcMessage;
import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.connect.entity.SimpleFuture;
import org.rpc.core.connect.netty.codec.RpcDecoder;
import org.rpc.core.connect.netty.codec.RpcEncoder;

import org.rpc.core.future.RpcFuture.FutureFactory;
import org.rpc.core.future.RpcFuture.RpcFuture;
import org.rpc.core.utils.ClientBlockQueueSingleton;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class NettyClient implements RequestTransport {

    private static final Logger logger= LogManager.getLogger(NettyClient.class);
    //发送请求后，等待获取结果的结果集
    public static final Map<Long,SimpleFuture> futureMap=new ConcurrentHashMap<>();
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
            //netty启动后通知发送请求
            ClientBlockQueueSingleton.getInstance().put("1");
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
        //链接服务端
        new Thread(this::start).start();
        //等待启动成功再发送
        try {
            ClientBlockQueueSingleton.getInstance().take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()));
        Long requestId = FutureFactory.put(future);
        RpcMessage rpcMessage = new RpcMessage(requestId,RpcMessage.REQUEST,rpcRequest);
        channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) channelFuture -> {
            if(channelFuture.isSuccess()){
                logger.info("发送成功");
            }else{
               logger.error("发送失败");
            }
        });
        //发送成功
        futureMap.put(requestId,new SimpleFuture());
        try {
            return futureMap.get(requestId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
//        try {
//            return future.getPromise().get();
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
    }
}

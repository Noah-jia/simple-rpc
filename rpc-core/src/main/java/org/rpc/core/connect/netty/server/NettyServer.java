package org.rpc.core.connect.netty.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.connect.netty.client.NettyClient;
import org.rpc.core.connect.netty.client.RequestHandler;
import org.rpc.core.connect.netty.codec.RpcDecoder;
import org.rpc.core.connect.netty.codec.RpcEncoder;
import org.rpc.core.serialize.Serialize;
import org.rpc.core.serialize.kryo.KryoSerialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@Order(1)
public class NettyServer {
    private static final Logger logger= LogManager.getLogger(NettyClient.class);
    private ServerBootstrap bootstrap;
    private NioEventLoopGroup boss;
    private NioEventLoopGroup worker;
    private Channel channel;



    @PostConstruct
    public void init(){
        new  Thread(()->{
            this.worker=new NioEventLoopGroup();
            this.boss=new NioEventLoopGroup();
            this.bootstrap=new ServerBootstrap();
            this.bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new RpcEncoder());
                            pipeline.addLast(new RpcDecoder());
                            pipeline.addLast(new ResponseHandler());
                        }
                    });
            try {
                logger.info("netty启动");
                channel=bootstrap.bind(8888).sync().channel();
                logger.info("有新客户端连接");
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }
        }).start();
    }
}

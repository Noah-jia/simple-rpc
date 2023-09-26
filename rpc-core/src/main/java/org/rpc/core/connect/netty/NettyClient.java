package org.rpc.core.connect.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.aspectj.lang.annotation.Before;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.connect.entity.RpcRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@Component
public class NettyClient implements RequestTransport {
    private Bootstrap bootstrap;
    private NioEventLoopGroup worker;

    @PostConstruct
    public void init(){
        this.worker=new NioEventLoopGroup();
        this.bootstrap=new Bootstrap();
        this.bootstrap.group(worker);

    }
    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest, InetSocketAddress inetSocketAddress) {
        return null;
    }
}

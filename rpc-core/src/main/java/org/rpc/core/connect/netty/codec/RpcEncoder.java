package org.rpc.core.connect.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.rpc.core.connect.entity.RpcMessage;
import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.serialize.Serialize;
import org.rpc.core.serialize.kryo.KryoSerialize;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Arrays;


/**
 * 把 message序列化成二进制
 */

public class RpcEncoder extends MessageToByteEncoder<RpcMessage> {
    private Serialize serialize;

    public RpcEncoder(){
        this.serialize=new KryoSerialize();
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage rpcMessage, ByteBuf byteBuf) throws Exception {
        // byte[] bytes = serialize.serialize(rpcRequest);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(rpcMessage);
        byte[] byteArray = baos.toByteArray();
        byteBuf.writeInt(byteArray.length);
        byteBuf.writeBytes(byteArray);
    }


}

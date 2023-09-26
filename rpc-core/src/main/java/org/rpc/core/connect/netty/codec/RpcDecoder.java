package org.rpc.core.connect.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rpc.core.connect.entity.RpcMessage;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.serialize.Serialize;
import org.rpc.core.serialize.kryo.KryoSerialize;
import org.rpc.core.utils.KryoSingle;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * 把返回结果序列化成Message
 */
public class RpcDecoder extends ByteToMessageDecoder {
    private static final Logger logger= LogManager.getLogger(RpcDecoder.class);
    private KryoSerialize serialize;
    public RpcDecoder() {
        this.serialize= KryoSingle.getInstance();
        System.out.println("decoder:"+serialize.kryo);
    }
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> list) throws Exception {
        if(in.readableBytes()>=4){
            in.markReaderIndex();
            int dataLength = in.readInt();
            if(dataLength<0||in.readableBytes()<0){
                logger.error("反序列化失败，消息长度错误");
                return;
            }
            //如果可读字节数小于消息长度的话，说明是不完整的消息，重置readIndex
            if(in.readableBytes()<dataLength){
                in.resetReaderIndex();
                return;
            }
            //序列化
            byte[] bytes = new byte[dataLength];
            in.readBytes(bytes);
//            RpcResponse response = serialize.deserialize(bytes, RpcResponse.class);
//            list.add(response);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            RpcMessage o = (RpcMessage) ois.readObject();
            list.add(o);
            logger.info("成功接收到了请求");
        }
    }
}

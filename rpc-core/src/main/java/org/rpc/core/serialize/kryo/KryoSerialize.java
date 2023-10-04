package org.rpc.core.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;
import org.rpc.core.connect.entity.RpcRequest;
import org.rpc.core.connect.entity.RpcResponse;
import org.rpc.core.serialize.Serialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;



public class KryoSerialize implements Serialize {
    public Kryo kryo;
//    private static final ThreadLocal<Kryo> kryoLocal=ThreadLocal.withInitial(()->{
//        Kryo kryo = new Kryo();
//        kryo.register(RpcRequest.class);
//        kryo.register(RpcResponse.class);
//        kryo.setReferences(false);
//        //默认值为false,是否关闭循环引用，可以提高性能
//       // kryo.setRegistrationRequired(false);
//        return kryo;
//    });
    public KryoSerialize(){
        this.kryo = new Kryo();
        this.kryo.register(RpcRequest.class);
        this.kryo.register(RpcResponse.class);
        this.kryo.setReferences(false);
    }

    @Override
    public byte[] serialize(RpcRequest rpcRequest) {
        this.kryo.register(RpcRequest.class);
        this.kryo.register(RpcResponse.class);
        try (ByteArrayOutputStream baos=new ByteArrayOutputStream();
             Output output=new Output(baos)){
            kryo.writeObject(output,rpcRequest);
            return output.toBytes();
        }catch (Exception e) {
            throw new SerializationException("序列号失败");
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        this.kryo.register(RpcRequest.class);
        this.kryo.register(RpcResponse.class);
        try(ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            Input input=new Input(bais)) {
            RpcResponse rpcResponse = kryo.readObject(input, RpcResponse.class);
            return clazz.cast(rpcResponse);
        }catch (Exception e){
           e.printStackTrace();
        }
        return null;
    }
}

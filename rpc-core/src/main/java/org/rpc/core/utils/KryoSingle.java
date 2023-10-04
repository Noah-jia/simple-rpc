package org.rpc.core.utils;

import com.esotericsoftware.kryo.Kryo;
import org.rpc.core.serialize.kryo.KryoSerialize;

public class KryoSingle {
    private static  volatile KryoSerialize kryo;
    private KryoSingle(){

    }
    public static KryoSerialize getInstance(){
        synchronized (KryoSerialize.class){
            if(kryo==null){
                synchronized (KryoSerialize.class) {
                    kryo = new KryoSerialize();
                }
            }
        }
        return kryo;
    }
}

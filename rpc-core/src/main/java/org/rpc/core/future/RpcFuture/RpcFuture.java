package org.rpc.core.future.RpcFuture;

import com.sun.org.apache.regexp.internal.RE;
import io.netty.util.concurrent.Promise;
import org.rpc.core.connect.netty.codec.RpcEncoder;

public class RpcFuture<T>{
    private Promise<T> promise;
    public RpcFuture(Promise<T>  promise){
        this.promise=promise;
    }
    public Promise<T> getPromise(){
        return  promise;
    }
    public void setPromise(Promise<T> promise){
        this.promise=promise;
    }
}

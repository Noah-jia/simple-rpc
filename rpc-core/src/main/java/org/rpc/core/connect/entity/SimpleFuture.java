package org.rpc.core.connect.entity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class SimpleFuture implements Future<RpcResponse> {

    private RpcResponse response;
    private   boolean isDone;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }
    public void done(){
        this.isDone=true;
        synchronized (this){
            this.notifyAll();
        }
    }

    @Override
    public boolean isDone() {
       return this.isDone;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
    }

    @Override
    public RpcResponse get() throws InterruptedException, ExecutionException {
        synchronized (this) {
            while (true) {
                if (!isDone) {
                    this.wait();
                }else{
                    //处理数据
                    return response;
                }
            }
        }
    }

    @Override
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}

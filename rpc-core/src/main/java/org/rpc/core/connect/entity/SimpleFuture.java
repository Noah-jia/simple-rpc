package org.rpc.core.connect.entity;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RpcFuture implements Future<RpcResponse> {
    private  Object lock=new Object();
    private   boolean isDone;
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public RpcResponse get() throws InterruptedException, ExecutionException {
        while (true){
            if(!isDone){
                
            }
        }
        return null;
    }

    @Override
    public RpcResponse get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}

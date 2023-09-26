package org.rpc.core.future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FutureFactory {
    public static final AtomicLong REQUEST_ID=new AtomicLong();

    public static final Map<Long, RpcFuture> REQUEST_MAP=new ConcurrentHashMap<>();

    public static Long put(RpcFuture future){
        long Rid = REQUEST_ID.incrementAndGet();
        REQUEST_MAP.put(Rid,future);
        return Rid;
    }

    public static RpcFuture get(Long id){
        return REQUEST_MAP.get(id);
    }

    public static void remove(Long id){
        REQUEST_MAP.remove(id);
    }
}

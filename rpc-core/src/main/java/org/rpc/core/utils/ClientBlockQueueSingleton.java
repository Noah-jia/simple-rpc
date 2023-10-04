package org.rpc.core.utils;

import java.util.concurrent.LinkedBlockingQueue;

public class ClientBlockQueueSingleton {
    private static volatile LinkedBlockingQueue<String> linkedBlockingQueue;
    private ClientBlockQueueSingleton(){
    }
    public static LinkedBlockingQueue<String> getInstance(){
        synchronized (ClientBlockQueueSingleton.class){
            if(linkedBlockingQueue==null){
                synchronized (ClientBlockQueueSingleton.class){
                    linkedBlockingQueue=new LinkedBlockingQueue<>();
                }
            }
        }
        return linkedBlockingQueue;
    }
}

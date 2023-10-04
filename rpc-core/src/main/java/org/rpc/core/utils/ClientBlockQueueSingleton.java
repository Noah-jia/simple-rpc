package org.rpc.core.utils;

import java.util.concurrent.LinkedBlockingQueue;

public class BlockQueueSingle {
    private static volatile LinkedBlockingQueue<String> linkedBlockingQueue;
    private BlockQueueSingle(){
    }
    public static LinkedBlockingQueue<String> getInstance(){
        synchronized (BlockQueueSingle.class){
            if(linkedBlockingQueue==null){
                synchronized (BlockQueueSingle.class){
                    linkedBlockingQueue=new LinkedBlockingQueue<>();
                }
            }
        }
        return linkedBlockingQueue;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                System.out.println(BlockQueueSingle.getInstance());
            }).start();
        }
    }
}

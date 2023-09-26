package org.rpc.core.test;


import org.rpc.core.annotation.RpcComponent;


@RpcComponent(version = "1")
public class Dog implements Animal{
    @Override
    public String say(String str) {
        return "我是一只:"+str;
    }
}

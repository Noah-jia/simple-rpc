package org.rpc.core.test;

import org.rpc.core.annotation.RpcAutowired;
import org.rpc.core.annotation.RpcComponent;
import org.springframework.stereotype.Component;

@RpcComponent(version = "1")
public class Dog implements Animal{
    @Override
    public String say(String str) {
        return "我是一只:"+str;
    }
}

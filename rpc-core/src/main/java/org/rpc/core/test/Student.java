package org.rpc.core.test;


import org.rpc.core.annotation.RpcAutowired;
import org.springframework.stereotype.Component;

@Component
public class Student implements People{
    @RpcAutowired
    private Dog dog;



    @Override
    public String say(String career) {
        return "我是一个:"+career;
    }
}

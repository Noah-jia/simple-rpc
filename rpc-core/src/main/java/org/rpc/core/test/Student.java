package org.rpc.core.test;


import org.rpc.core.annotation.RpcAutowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class Student implements People{
    @RpcAutowired
    private Animal animal;



    @Override
    public String say(String career) {
        return "我是一个:"+career+"animal说:"+animal.say("wang");
    }
}

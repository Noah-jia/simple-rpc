package org.rpc.core.config;


import org.rpc.core.spring.RpcBeanAware;
import org.rpc.core.test.Dog;
import org.rpc.core.test.People;
import org.rpc.core.test.Student;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;


@ComponentScan({"org.rpc.core.*"})
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringConfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        People people = context.getBean("student", Student.class);
       // Dog dog = context.getBean("dog", Dog.class);
       // System.out.println(dog.say("wocao"));
//        System.out.println(dog.say("汪"));
//        System.out.println(people.say("人"));
    }
}

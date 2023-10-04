package org.rpc.core.spring;


import org.rpc.core.test.Student;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@ComponentScan({"org.rpc.core.*"})
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringConfig {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        //Dog dog = context.getBean("dog", Dog.class);
        Student student = context.getBean("student", Student.class);
        System.out.println(student.say("人"));
        for (int i = 0; i < 100; i++) {
            System.out.println(student.say(String.valueOf(i)));
        }
        //System.out.println(dog.say("wang"));
    }
}

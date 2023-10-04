package org.rpc.core.spring;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.rpc.core.annotation.RpcAutowired;
import org.rpc.core.annotation.RpcComponent;
import org.rpc.core.config.RpcBeanConfig;
import org.rpc.core.config.RpcConstants;
import org.rpc.core.connect.RequestTransport;
import org.rpc.core.proxy.client.RpcBeanProxy;
import org.rpc.core.utils.ZkUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.net.InetAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RpcBeanAware implements BeanPostProcessor{
    public static Map<String,Object> serviceMap=new ConcurrentHashMap<>();
    private static final Logger logger= LogManager.getLogger(RpcBeanAware.class);

    @Autowired
    private RequestTransport requestTransport;
    @Autowired
    private ZkUtils zkUtils;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        handleServiceAnnotation(bean, beanName);
        try {
            return handleClientAnnotation(bean, beanName);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private  Object handleClientAnnotation(Object bean, String beanName) throws IllegalAccessException {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation fieldAnnotation : fieldAnnotations) {
                if(fieldAnnotation instanceof  RpcAutowired){
                    //远程调用方法
                    logger.info("扫描到RpcAutowired:"+ beanName);
                    RpcAutowired rpcAutowired=(RpcAutowired) fieldAnnotation;
                    String version = rpcAutowired.version();
                    Class<?> type = field.getType();
                    RpcBeanConfig rpcBeanConfig = null;
                    try {
                        rpcBeanConfig = new RpcBeanConfig(type, version);
                         RpcBeanProxy rpcBeanProxy = new RpcBeanProxy(rpcBeanConfig,requestTransport,zkUtils);
                        Object proxy = getProxy(type,rpcBeanProxy);
                        field.setAccessible(true);
                        field.set(bean,proxy);
                        // Can not set org.rpc.core.test.Dog field org.rpc.core.test.Student.dog to com.sun.proxy.$Proxy41
                    } catch (IllegalAccessException  e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return bean;
    }
    public <T> T getProxy(Class<T> clazz,RpcBeanProxy rpcBeanProxy) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, rpcBeanProxy);
    }

    private  void handleServiceAnnotation(Object bean, String beanName) {
        Annotation[] beanAnnotations = bean.getClass().getAnnotations();
        for (Annotation beanAnnotation : beanAnnotations) {
            if(beanAnnotation instanceof RpcComponent){
                //发布到注册中心，并保存到一个本地Map中，等待调用
                logger.info("扫描到RpcComponent:"+ beanName);
                RpcComponent  rpcComponent=(RpcComponent) beanAnnotation;
                String version = rpcComponent.version();
                RpcBeanConfig rpcBeanConfig = new RpcBeanConfig(bean, version);
                String interfaceName = bean.getClass().getInterfaces()[0].getCanonicalName();
                serviceMap.put(interfaceName + RpcConstants.SERVICE_SPLIT+version, rpcBeanConfig);
                //注册到Zk
                toZk(rpcBeanConfig);
            }
        }
    }

    /**
     * 注册到Zk
     * @param rpcBeanConfig
     */
    private void toZk(RpcBeanConfig rpcBeanConfig) {
        zkUtils.createPersistentNode(rpcBeanConfig.getInterfaceName(),rpcBeanConfig.getVersion());
    }
}

package org.rpc.core.config;

public class RpcBeanConfig {
    private String version;
    private Object object;
    public RpcBeanConfig(Object object,String version){
        this.version=version;
        this.object=object;
    }
    public String getInterfaceName(){
        return  object.getClass().getInterfaces()[0].getCanonicalName();
    }
    public String getFullBeanName(){
        return getInterfaceName()+RpcConstants.SERVICE_SPLIT+version;
    }

    public Object getObject() {
        return object;
    }

    public String getVersion() {
        return version;
    }
}

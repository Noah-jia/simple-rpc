package org.rpc.core.loadbalance;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Random  implements LoadBalance{
    @Override
    public String getOne(List<String> list) {
        return list.get(0);
    }
}

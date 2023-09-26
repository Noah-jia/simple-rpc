package org.rpc.core.loadbalance;

import java.util.List;

public interface LoadBalance {
    String getOne(List<String > list);
}

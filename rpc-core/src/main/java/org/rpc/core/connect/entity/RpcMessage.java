package org.rpc.core.connect.entity;

import java.io.Serializable;

public class RpcMessage implements Serializable {
    public static final int REQUEST=0;
    public static final  int RESPONSE=1;
    Long requestId;
    int type;
    Object content;

    public RpcMessage(Long requestId, int type, Object content) {
        this.requestId = requestId;
        this.type = type;
        this.content = content;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}

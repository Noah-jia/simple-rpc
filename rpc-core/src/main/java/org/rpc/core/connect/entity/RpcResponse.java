package org.rpc.core.connect.entity;

import java.io.Serializable;

public class RpcResponse implements Serializable {
    private static final long serialVersionUID = 715745410605631233L;
    private String requestId;
    /**
     * response code
     */
    private Integer code;
    /**
     * response message
     */
    private String message;
    /**
     * response body
     */
    private Object data;
    public   RpcResponse success(Object data, String requestId) {
        RpcResponse response = new RpcResponse();
        response.setCode(200);
        response.setMessage("成功");
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }
    public   RpcResponse fail() {
        RpcResponse response = new RpcResponse();
        response.setCode(400);
        response.setMessage("失败");
        return response;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/20.
 */
public class AcquireBean {
    private String msg;
    private String status;

    private List<AcquireList> acquireListList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AcquireList> getAcquireListList() {
        return acquireListList;
    }

    public void setAcquireListList(List<AcquireList> acquireListList) {
        this.acquireListList = acquireListList;
    }

    @Override
    public String toString() {
        return "AcquireBean{" +
                "msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                ", acquireListList=" + acquireListList +
                '}';
    }
}

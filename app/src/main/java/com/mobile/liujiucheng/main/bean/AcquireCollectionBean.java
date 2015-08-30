package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/23.
 */
public class AcquireCollectionBean {
    private String msg;
    private String status;
    private List<AcquireCollectionListBean> list;

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

    public List<AcquireCollectionListBean> getList() {
        return list;
    }

    public void setList(List<AcquireCollectionListBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "AcquireCollectionBean{" +
                "msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                ", list=" + list +
                '}';
    }
}

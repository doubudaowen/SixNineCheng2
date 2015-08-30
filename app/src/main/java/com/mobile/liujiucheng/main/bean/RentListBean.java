package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/13.
 */

public class RentListBean {
    String msg;
    String status;
    String error;
    List<RentListDataBean> data;

    public String getMsg() {
        return msg;
    }

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public List<RentListDataBean> getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setData(List<RentListDataBean> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RentListBean{" +
                "msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                ", error='" + error + '\'' +
                ", data=" + data +
                '}';
    }
}

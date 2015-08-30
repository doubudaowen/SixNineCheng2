package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/19.
 */
public class PandaSelectCollection {
//    "msg": "您的收藏",
//            "status": "Y",
//            "error": null,
//            "data": [
//    {

    private String msg;
    private String status;
    private List<PandaCollectionBean> list;

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

    public List<PandaCollectionBean> getList() {
        return list;
    }

    public void setList(List<PandaCollectionBean> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PandaSelectCollection{" +
                "msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                ", list=" + list +
                '}';
    }
}

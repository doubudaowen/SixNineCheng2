package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import java.util.List;

/**
 * Created by pc on 2015/7/8.
 */
public class Dialog_forlist {
    private String name;
    private List<String> list;
    private List<Integer> list_id;

    @Override
    public String toString() {
        return "Dialog_forlist{" +
                "name='" + name + '\'' +
                ", list=" + list +
                ", list_id=" + list_id +
                '}';
    }

    public List<Integer> getList_id() {
        return list_id;
    }

    public void setList_id(List<Integer> list_id) {
        this.list_id = list_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}

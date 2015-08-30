package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/10.
 */
public class SchoolBean {
    private String provinceName;
    private List<Schools> school;

    public String getProvinceName() {
        return provinceName;
    }

    public List<Schools> getSchool() {
        return school;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public void setSchool(List<Schools> school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "SchoolBean{" +
                "provinceName='" + provinceName + '\'' +
                ", school=" + school +
                '}';
    }
}

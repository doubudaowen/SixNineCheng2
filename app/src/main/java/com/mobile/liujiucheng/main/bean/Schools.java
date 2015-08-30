package com.mobile.liujiucheng.main.bean;

/**
 * Created by DOUjunjun on 2015/7/10.
 */
public class Schools {
    private String schoolName;
    private int schoolId;

    public String getSchoolName() {
        return schoolName;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    @Override
    public String toString() {
        return "Schools{" +
                "schoolName='" + schoolName + '\'' +
                ", schoolId=" + schoolId +
                '}';
    }
}

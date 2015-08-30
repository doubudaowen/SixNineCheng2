package com.mobile.liujiucheng.main.bean;

/**
 * Created by DOUjunjun on 2015/7/10.
 */
public class Citys {
    private int cityId;
    private String cityName;

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "Citys{" +
                "cityId=" + cityId +
                ", cityName='" + cityName + '\'' +
                '}';
    }
}

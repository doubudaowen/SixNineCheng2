package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/9.
 */
public class PlaceBean {
    private String privaceName;
    private List<Citys> city;

    public String getPrivaceName() {
        return privaceName;
    }

    public List<Citys> getCity() {
        return city;
    }

    public void setPrivaceName(String privaceName) {
        this.privaceName = privaceName;
    }

    public void setCity(List<Citys> city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "PlaceBean{" +
                "privaceName='" + privaceName + '\'' +
                ", city=" + city +
                '}';
    }
}

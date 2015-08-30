package com.mobile.liujiucheng.main.bean;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/15.
 */

public class ItemBean {
    private String msg;
    private String status;
    private int id;
    private String title;
    private String linkMan;
    private String linkPhone;
    private int rent;
    private int payStyle;
    private int statuss;
    private String houseType;
    private int spec;
    private int rentFloor;
    private int floor;
    private int rentStyle;
    //private int tenementType;
    //private String interiordesign;
    private String config;
    private String aspect;
    private String description;
    private String address;
    private String city;
    private String district;
    //    private String tenement;
    private String bussinessarea;
    //    private int infoSource;
//    private int enterStatus;
//    private int isDeal;
//    private int isIndex;
//    private int isTop;
    private String zuobiaoX;
    private String zuobiaoY;
    //    private String phoneInfo;
//    private int phoneType;
//    private int click;
//    private String imgUrl;
//    private int num;
//    private String bidid;

    private List<ListTenant> imgList;

    private List<ImageBean> imageLists;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(int payStyle) {
        this.payStyle = payStyle;
    }

    public int getStatuss() {
        return statuss;
    }

    public void setStatuss(int statuss) {
        this.statuss = statuss;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public int getSpec() {
        return spec;
    }

    public void setSpec(int spec) {
        this.spec = spec;
    }

    public int getRentFloor() {
        return rentFloor;
    }

    public void setRentFloor(int rentFloor) {
        this.rentFloor = rentFloor;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRentStyle() {
        return rentStyle;
    }

    public void setRentStyle(int rentStyle) {
        this.rentStyle = rentStyle;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBussinessarea() {
        return bussinessarea;
    }

    public void setBussinessarea(String bussinessarea) {
        this.bussinessarea = bussinessarea;
    }

    public String getZuobiaoX() {
        return zuobiaoX;
    }

    public void setZuobiaoX(String zuobiaoX) {
        this.zuobiaoX = zuobiaoX;
    }

    public String getZuobiaoY() {
        return zuobiaoY;
    }

    public void setZuobiaoY(String zuobiaoY) {
        this.zuobiaoY = zuobiaoY;
    }

    public List<ListTenant> getImgList() {
        return imgList;
    }

    public void setImgList(List<ListTenant> imgList) {
        this.imgList = imgList;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "msg='" + msg + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", linkPhone='" + linkPhone + '\'' +
                ", rent=" + rent +
                ", payStyle=" + payStyle +
                ", statuss=" + statuss +
                ", houseType='" + houseType + '\'' +
                ", spec=" + spec +
                ", rentFloor=" + rentFloor +
                ", floor=" + floor +
                ", rentStyle=" + rentStyle +
                ", config='" + config + '\'' +
                ", aspect='" + aspect + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", bussinessarea='" + bussinessarea + '\'' +
                ", zuobiaoX='" + zuobiaoX + '\'' +
                ", zuobiaoY='" + zuobiaoY + '\'' +
                ", imgList=" + imgList +
                '}';
    }
}
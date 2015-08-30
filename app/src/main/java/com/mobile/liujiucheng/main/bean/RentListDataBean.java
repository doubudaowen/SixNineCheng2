package com.mobile.liujiucheng.main.bean;

/**
 * Created by DOUjunjun on 2015/7/13.
 */
public class RentListDataBean {
    private int id;                   //
    private String title;            //
    private String linkMan;          //
    //    private String linkPhone;
    private String rent;             //
    //    private String payStyle;
//    private String pubTime;
//    private String status;
    private String houseType;        //
    private String spec;             //
    private int rentFloor;          //
    private int floor;              //
    private int rentStyle;         //
    //    private String tenementType;
//    private String interiordesign;
//    private String config;
//    private String aspect;
//    private String description;
//    private String address;
//    private String city;
//    private String district;
//    private String tenement;
//    private String bussinessarea;
//    private String infoSource;
//    private String enterStatus;
//    private String tuserId;
//    private String isDeal;
//    private String subTime;
//    private String isIndex;
//    private String isTop;
//    private String zuobiaoX;
//    private String zuobiaoY;
//    private String rejectReason;
//    private String phoneInfo;
//    private String phoneType;
    private String click;               //
    private String imgUrl;              //
    //    private String imgPath;
    private String num;                 //
    //    private String indexFlag;
    private String characteristic;     //
    //    private String zuobiaoXD;
//    private String zuobiaoYD;
//    private String startX;
//    private String endX;
//    private String startY;
//    private String endY;
//    private String pageNo;
//    private String pageSize;
//    private String minRent;
//    private String maxRent;
//    private String minSpec;
//    private String maxSpec;
//    private String houseTypeValue;
//    private String sumHouse;
//    private String sort;
//    private String order;
//    private String countOfTel;
//    private String imageList;
//    private String bidid;
//    private String listTenant;
    private String age;                //
    private String job;                //

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

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
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

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public String toString() {
        return "RentListDataBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", rent='" + rent + '\'' +
                ", houseType='" + houseType + '\'' +
                ", spec='" + spec + '\'' +
                ", rentFloor=" + rentFloor +
                ", floor=" + floor +
                ", rentStyle=" + rentStyle +
                ", click='" + click + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", num='" + num + '\'' +
                ", characteristic='" + characteristic + '\'' +
                ", age='" + age + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
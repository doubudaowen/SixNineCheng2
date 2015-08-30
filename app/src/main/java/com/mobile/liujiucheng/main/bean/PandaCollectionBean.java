package com.mobile.liujiucheng.main.bean;

/**
 * Created by DOUjunjun on 2015/7/19.
 */
public class PandaCollectionBean {

    private int id;                   //
    private String title;            //
    private String linkMan;          //
    private String rent;             //
    private String houseType;        //
    private String spec;             //
    private int rentFloor;          //
    private int floor;              //
    private int rentStyle;         //
    private String imgUrl;              //
    private String characteristic;     //
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
        return "PandaCollectionBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", rent='" + rent + '\'' +
                ", houseType='" + houseType + '\'' +
                ", spec='" + spec + '\'' +
                ", rentFloor=" + rentFloor +
                ", floor=" + floor +
                ", rentStyle=" + rentStyle +
                ", imgUrl='" + imgUrl + '\'' +
                ", characteristic='" + characteristic + '\'' +
                ", age='" + age + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}

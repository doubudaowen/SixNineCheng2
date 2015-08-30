package com.mobile.liujiucheng.main.bean;

/**
 * Created by DOUjunjun on 2015/7/20.
 */
public class AcquireList {
    private int id;//
    private String code;//
    private String city;//
    private String district;//
    private String business;//
    private String rent;//
    private String sex;//
    private String age;//
    private String interest;//
    private String constellation;//
    private String schoolName;//
    private String jobName;//
    private String nativePlace;//
    private String imgUrl;//
    private String checkInTimeStr;//
    private String name;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCheckInTimeStr() {
        return checkInTimeStr;
    }

    public void setCheckInTimeStr(String checkInTimeStr) {
        this.checkInTimeStr = checkInTimeStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "AcquireList{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", business='" + business + '\'' +
                ", rent='" + rent + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", interest='" + interest + '\'' +
                ", constellation='" + constellation + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", jobName='" + jobName + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", checkInTimeStr='" + checkInTimeStr + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
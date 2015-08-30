package com.mobile.liujiucheng.main.bean;

/**
 * Created by DOUjunjun on 2015/7/23.
 */
public class AcquireCollectionListBean {
    private String code;
    private String nativePlace;
    private int id;
    private String constellation;
    private String jobName;
    private String sex;
    private String district;
    private String city;
    private String age;
    private String interest;
    private String rent;
    private String checkInTimeStr;
    private String schoolName;
    private String name;
    private String title;
    private String imgUrl;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getCheckInTimeStr() {
        return checkInTimeStr;
    }

    public void setCheckInTimeStr(String checkInTimeStr) {
        this.checkInTimeStr = checkInTimeStr;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "AcquireCollectionListBean{" +
                "code='" + code + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", id=" + id +
                ", constellation='" + constellation + '\'' +
                ", jobName='" + jobName + '\'' +
                ", sex='" + sex + '\'' +
                ", district='" + district + '\'' +
                ", city='" + city + '\'' +
                ", age='" + age + '\'' +
                ", interest='" + interest + '\'' +
                ", rent='" + rent + '\'' +
                ", checkInTimeStr='" + checkInTimeStr + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}

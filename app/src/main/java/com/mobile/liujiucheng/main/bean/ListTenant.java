package com.mobile.liujiucheng.main.bean;

/**
 * Created by DOUjunjun on 2015/7/18.
 */
public class ListTenant {
//    "id": 16,
//            "code": null,
//            "city": null,
//            "district": null,
//            "business": null,
//            "houseType": null,
//            "rent": null,
//            "name": null,
//            "sex": "男",
//            "age": "70后",
//            "phone": null,
//            "interest": null,
//            "constellation": "天蝎座",
//            "nativePlace": 56,
//            "school": 0,
//            "job": 25,
//            "description": null,
//            "checkInTime": null,
//            "createTime": null,
//            "status": 0,
//            "appendixUploadId": 0,
//            "userId": 0,
//            "houseId": 0,
//            "houseName": "主卧",
//            "type": 0,
//            "schoolName": null,
//            "jobName": null,
//            "pageNo": 0,
//            "pageSize": 12,
//            "checkInTimeF": null,
//            "imgUrl": "http://114.215.102.96:8288/69cheng-app/images/hezu.jpg",
//            "listAU": []
    private int id;
    private String sex;
    private String age;
    private String constellation;
    private String jobName;
    private String nativePlaceName;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getNativePlaceName() {
        return nativePlaceName;
    }

    public void setNativePlaceName(String nativePlaceName) {
        this.nativePlaceName = nativePlaceName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "ListTenant{" +
                "id=" + id +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", constellation='" + constellation + '\'' +
                ", jobName='" + jobName + '\'' +
                ", nativePlaceName='" + nativePlaceName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}

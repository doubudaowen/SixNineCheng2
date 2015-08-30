package com.mobile.liujiucheng.main.bean.base;

/**
 * Created by DOUjunjun on 2015/7/8.
 */
public class JobBean {
    private String jobId;
    private String jobName;

    public JobBean(String jobId, String jobName) {
        this.jobId = jobId;
        this.jobName = jobName;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "JobBean{" +
                "jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }
}

package com.mobile.liujiucheng.main.bean.base;

/**
 * Created by DOUjunjun on 2015/7/27.
 */
public class JobNewBean {
    private String jobId;
    private String jobName;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public String toString() {
        return "JobNewBean{" +
                "jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +
                '}';
    }
}

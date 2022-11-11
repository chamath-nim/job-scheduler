package com.mobitel.jobscheduler.domain;

import lombok.Data;

@Data
public class JobDetails {

    private String schedulerName;
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
}

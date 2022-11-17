package com.mobitel.jobscheduler.domain;

import lombok.Data;

import java.util.Date;

@Data
public class MainJobs {

    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String triggerDescription;
    private String triggerState;
    private String triggerType;
    private String jobClassName;
    private String cronExpression;
    private Date nextFireTime;
    private Date previousFireTime;
    private Date startTime;

}

package com.mobitel.jobscheduler.domain;

import lombok.Data;

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
    private String nextFireTime;
    private String previousFireTime;
    private String startTime;

}

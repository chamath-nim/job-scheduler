package com.mobitel.jobscheduler.domain;

import lombok.Data;

@Data
public class Jobs {

    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String cronExpression;
}

package com.mobitel.jobscheduler.domain;

import lombok.Data;

@Data
public class Triggers {

    private String schedulerName;
    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String description;
    private String triggerState;
    private String triggerType;
}

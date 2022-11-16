package com.mobitel.jobscheduler.dto;

import lombok.Data;
@Data
public class JobsDTO {

    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String cronExpression;
}

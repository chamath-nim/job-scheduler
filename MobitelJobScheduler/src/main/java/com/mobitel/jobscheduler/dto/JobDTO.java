package com.mobitel.jobscheduler.dto;

import lombok.Data;
@Data
public class JobDTO {

    private String triggerName;
    private String triggerGroup;
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String cronExpression;

    private Long firedJobId;
    private Long ServiceRequestId;
    private String status;
    private String actualStartTime;
    private String endTime;
    private String startTime;
    private int notifyCount;
}

package com.mobitel.jobscheduler.domain;

import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime nextFireTime;
    private LocalDateTime previousFireTime;
    private LocalDateTime startTime;
    
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;

    private boolean repeating;
    private String repeatUnit;
    private int repeatInterval;
    private int repetitions;
}

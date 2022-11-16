package com.mobitel.jobscheduler.dto;

import lombok.Data;

@Data
public class FiredJobsDTO {

    private Long firedJobId;
    private Long ServiceRequestId;
    private String status;
    private String actualStartTime;
    private String endTime;
    private String startTime;
    private int notifyCount;
}

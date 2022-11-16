package com.mobitel.jobscheduler.dto;

import lombok.Data;

@Data
public class ServiceRequestsDTO {

    private Long id;
    private String status;
    private String subStatus;
    private String requestTime;
    private int notifyCount;
}

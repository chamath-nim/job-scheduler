package com.mobitel.jobscheduler.util.generic;

import lombok.Data;

@Data
public class RequestHeader {
    private String transactionId;
    private String requestTime;
    private String requestedBy;
    private String operationType;
}

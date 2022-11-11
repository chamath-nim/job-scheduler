package com.mobitel.servicereq.util;

import lombok.Data;

@Data
public class RequestHandler {
    private String jobName;
    private String jobGroupName;
    private String triggerName;
    private String triggerGroupName;
    private String triggerDescription;
    private String cronExpression;
}

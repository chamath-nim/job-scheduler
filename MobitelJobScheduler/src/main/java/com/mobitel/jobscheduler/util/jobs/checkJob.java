package com.mobitel.jobscheduler.util.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class checkJob extends QuartzJobBean {

    Logger logger = LoggerFactory.getLogger(checkJob.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
       logger.info("works");
    }
}

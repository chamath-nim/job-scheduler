package com.mobitel.jobscheduler.service;

import com.mobitel.jobscheduler.dto.JobDTO;
import com.mobitel.jobscheduler.util.generic.RequestHandler;
import com.mobitel.jobscheduler.util.generic.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Slf4j
@Service
public class MainJobsService {

    @Autowired
    private Scheduler scheduler;

    Logger logger = LoggerFactory.getLogger(MainJobsService.class);
    private Class<Job> job;


    public JobDetail mainJobDetail(Class<? extends Job> jobClass, String name, String group){
        return newJob(jobClass)
                .withIdentity(name,group)
                .build();
    }

    public Trigger mainTrigger(String name, String group, String description, JobDetail jobDetail, String cronExpression){
        return newTrigger()
                .withIdentity(name,group)
                .withDescription(description)
                .withSchedule(cronSchedule(cronExpression))
                .forJob(jobDetail)
                .build();
    }

    public ResponseHandler<String> mainScheduler(RequestHandler<JobDTO> jobDTORequestHandler){
        ResponseHandler<String> jobDTOResponseHandler = new ResponseHandler<>();

        try {
            JobDTO jobDTO = jobDTORequestHandler.getBody();
            JobDetail jobDetail = mainJobDetail(Class.forName(jobDTO.getJobClassName()).asSubclass(job),
                                                jobDTO.getJobName(), jobDTO.getJobGroup());

            Trigger trigger = mainTrigger(jobDTO.getJobName(), jobDTO.getJobGroup(), jobDTO.getDescription(),
                                          jobDetail, jobDTO.getCronExpression());

            scheduler.scheduleJob(jobDetail, trigger);
            jobDTOResponseHandler.setBody("Successfully created scheduled the job");

        }
        catch (SecurityException | SchedulerException | ClassNotFoundException e){
            logger.error(String.valueOf(e));
        }
        return jobDTOResponseHandler;
    }
}

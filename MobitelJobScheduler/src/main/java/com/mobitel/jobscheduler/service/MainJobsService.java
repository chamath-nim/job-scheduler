package com.mobitel.jobscheduler.service;

import com.mobitel.jobscheduler.domain.Jobs;
import com.mobitel.jobscheduler.dto.JobsDTO;
import com.mobitel.jobscheduler.util.generic.RequestHandler;
import com.mobitel.jobscheduler.util.generic.ResponseHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(MainJobsService.class);

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

    public ResponseHandler<String> mainScheduler(RequestHandler<JobsDTO> jobDTORequestHandler){
        ResponseHandler<String> jobDTOResponseHandler = new ResponseHandler<>();

        try {
            Jobs job = modelMapper.map(jobDTORequestHandler.getBody(), Jobs.class);

            JobDetail jobDetail = mainJobDetail(Class.forName(job.getJobClassName()).asSubclass(Job.class),
                    job.getJobName(), job.getJobGroup());

            Trigger trigger = mainTrigger(job.getTriggerName(), job.getTriggerGroup(), job.getDescription(),
                                          jobDetail, job.getCronExpression());

            scheduler.scheduleJob(jobDetail, trigger);
            jobDTOResponseHandler.setBody("Successfully created scheduled the job");

        }
        catch (SecurityException | SchedulerException | ClassNotFoundException e){
            logger.error(String.valueOf(e));
        }
        return jobDTOResponseHandler;
    }
}

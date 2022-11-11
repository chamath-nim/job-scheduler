package com.mobitel.jobscheduler.service;

import com.mobitel.jobscheduler.dto.JobDTO;
import com.mobitel.jobscheduler.util.generic.RequestHandler;
import com.mobitel.jobscheduler.util.generic.ResponseHandler;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class ServiceRequestService {
    @Autowired
    private Scheduler scheduler;
    Logger logger = LoggerFactory.getLogger(ServiceRequestService.class);

    public ResponseHandler<String> deleteJob(RequestHandler<JobDTO> jobDTORequestHandler){
        ResponseHandler<String> jobDTOResponseHandler = new ResponseHandler<>();

        String jobGroup = jobDTORequestHandler.getBody().getJobGroup();
        try {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup))) {
                System.out.println(jobKey.getName());
                scheduler.deleteJob(jobKey);
                jobDTOResponseHandler.setBody(jobKey.getName()+" job deleted");
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
        return jobDTOResponseHandler;
    }

    public ResponseHandler<String> pauseTrigger(RequestHandler<JobDTO> jobDTORequestHandler){
        ResponseHandler<String> jobDTOResponseHandler = new ResponseHandler<>();

        String triggerGroup = jobDTORequestHandler.getBody().getTriggerGroup();
        try {
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup))) {
                System.out.println(triggerKey.getName());
                scheduler.pauseTrigger(triggerKey);
                jobDTOResponseHandler.setBody("Reccuring job paused");
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
        return jobDTOResponseHandler;
    }

    public ResponseHandler<String> resumeTrigger(RequestHandler<JobDTO> jobDTORequestHandler){
        ResponseHandler<String> jobDTOResponseHandler = new ResponseHandler<>();

        String triggerGroup = jobDTORequestHandler.getBody().getTriggerGroup();
        try {
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup))) {
                System.out.println(triggerKey.getName());
                scheduler.resumeTrigger(triggerKey);
                jobDTOResponseHandler.setBody("Reccuring job resumed");
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
        return jobDTOResponseHandler;
    }

    public ResponseHandler<String> deleteAllJobs(){
        ResponseHandler<String> jobDTOResponseHandler = new ResponseHandler<>();

        try {
            List<JobKey> jobKeyList = new ArrayList<>();
            for (String groupName : scheduler.getJobGroupNames()) {
                jobKeyList.addAll(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)));
            }
            scheduler.deleteJobs(jobKeyList);
            jobDTOResponseHandler.setBody("All jobs deleted");
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
        return jobDTOResponseHandler;
    }

//    public void addRequst(SR sr){
//        sr.setRequestTime(CurrentDateTime());
//        srRepo.save(sr);
//    }

//    public List<JobDetails> getFiredJobs(int count){
//        return firedJobsRepo.getFiredJobs(count);
//    }

//    public List<MainJobs> getJobs(){
//        List<MainJobs> jobs = new ArrayList<>();
//        try {
//            for (String groupName : scheduler.getJobGroupNames()) {
//                if (!groupName.equals("jobST")) {
//                    for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
//                        MainJobs mainJobs = new MainJobs();
//
//                        mainJobs.setTiggerName(triggerKey.getName());
//                        mainJobs.setTriggerGroup(triggerKey.getGroup());
//                        mainJobs.setTriggerState(String.valueOf(scheduler.getTriggerState(triggerKey)));
//                        mainJobs.setDescription(scheduler.getTrigger(triggerKey).getDescription());
//
//                        jobs.add(mainJobs);
//                    }
//                }
//            }
//        }
//        catch (SchedulerException e){
//            logger.error(String.valueOf(e));
//        }
//        return jobs;
//    }

    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}

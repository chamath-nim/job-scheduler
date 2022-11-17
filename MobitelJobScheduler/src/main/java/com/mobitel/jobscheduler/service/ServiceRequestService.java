package com.mobitel.jobscheduler.service;

import com.mobitel.jobscheduler.domain.FiredJobs;
import com.mobitel.jobscheduler.domain.MainJobs;
import com.mobitel.jobscheduler.domain.ServiceRequests;
import com.mobitel.jobscheduler.dto.FiredJobsDTO;
import com.mobitel.jobscheduler.dto.JobsDTO;
import com.mobitel.jobscheduler.dto.MainJobsDTO;
import com.mobitel.jobscheduler.dto.ServiceRequestsDTO;
import com.mobitel.jobscheduler.repository.FiredJobsRepo;
import com.mobitel.jobscheduler.repository.ServiceRequestRepo;
import com.mobitel.jobscheduler.util.generic.RequestHandler;
import com.mobitel.jobscheduler.util.generic.ResponseHandler;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
public class ServiceRequestService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private FiredJobsRepo firedJobsRepo;

    @Autowired
    private ServiceRequestRepo serviceRequestRepo;

    @Autowired
    private ModelMapper modelMapper;

    Logger logger = LoggerFactory.getLogger(ServiceRequestService.class);

    public ResponseHandler<String> deleteJob(RequestHandler<JobsDTO> jobDTORequestHandler){
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

    public ResponseHandler<String> pauseTrigger(RequestHandler<JobsDTO> jobDTORequestHandler){
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

    public ResponseHandler<String> resumeTrigger(RequestHandler<JobsDTO> jobDTORequestHandler){
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

    public ResponseHandler<ServiceRequestsDTO> addRequst(RequestHandler<ServiceRequestsDTO> serviceRequestsDTORequestHandler){
        ResponseHandler<ServiceRequestsDTO> serviceRequestsDTOResponseHandler = new ResponseHandler<>();

        ServiceRequests serviceRequests = modelMapper.map(serviceRequestsDTORequestHandler.getBody(), ServiceRequests.class);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        serviceRequests.setSr_CREATED_ON(LocalDateTime.now());
        serviceRequests.setSr_COMMITED_DATE(LocalDateTime.now().plusHours(24));
        serviceRequestRepo.save(serviceRequests);

        serviceRequestsDTOResponseHandler.setBody(modelMapper.map(serviceRequests,ServiceRequestsDTO.class));
        return serviceRequestsDTOResponseHandler;
    }

    public ResponseHandler<FiredJobsDTO> getFiredJobs(int count){
        ResponseHandler<FiredJobsDTO> firedJobsDTOResponseHandler = new ResponseHandler<>();

        List<FiredJobs> firedJobs = firedJobsRepo.getFiredJobs(count);
        TypeToken<List<FiredJobsDTO>> typeToken = new TypeToken<>() {};
        List<FiredJobsDTO> firedJobsDTOList = modelMapper.map(firedJobs, typeToken.getType());
        firedJobsDTOResponseHandler.setParaList(firedJobsDTOList);

        return firedJobsDTOResponseHandler;
    }

    public ResponseHandler<MainJobsDTO> getJobs(){
        ResponseHandler<MainJobsDTO> mainJobsDTOResponseHandler = new ResponseHandler<>();

        List<MainJobs> jobs = new ArrayList<>();
        try {
            for (String groupName : scheduler.getTriggerGroupNames()) {
                if (!groupName.equals("jobST")) {
                    MainJobs mainJobs = new MainJobs();

                    for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
                        Trigger trigger = scheduler.getTrigger(triggerKey);

                        mainJobs.setTriggerName(triggerKey.getName());

                        mainJobs.setTriggerGroup(triggerKey.getGroup());

                        mainJobs.setTriggerState(String.valueOf(scheduler.getTriggerState(triggerKey)));

                        mainJobs.setTriggerDescription(trigger.getDescription());

                        mainJobs.setNextFireTime(ConvetMilliSecToDate(trigger.getNextFireTime().getTime()));

                        Date preTime;
                        try {
                            preTime = ConvetMilliSecToDate(trigger.getPreviousFireTime().getTime());
                        }
                        catch (NullPointerException e){
                            preTime = null;
                        }

                        mainJobs.setPreviousFireTime(preTime);

                        mainJobs.setStartTime(ConvetMilliSecToDate(trigger.getStartTime().getTime()));

                        mainJobs.setJobName(trigger.getJobKey().getName());

                        mainJobs.setJobGroup(trigger.getJobKey().getGroup());

                        if (trigger instanceof CronTrigger) {
                            CronTrigger cronTrigger = (CronTrigger) trigger;
                            mainJobs.setCronExpression(cronTrigger.getCronExpression());
                            mainJobs.setTriggerType("Cron");
                        }
                        else {
                            mainJobs.setCronExpression("");
                            mainJobs.setTriggerType("Simple");
                        }
                        mainJobs.setJobClassName(String.valueOf(scheduler.getJobDetail(trigger.getJobKey()).getJobClass()));
                    }
                    jobs.add(mainJobs);
                }
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
        TypeToken<List<MainJobsDTO>> typeToken = new TypeToken<>() {
        };
        List<MainJobsDTO> mainJobsDTOList = modelMapper.map(jobs, typeToken.getType());
        mainJobsDTOResponseHandler.setParaList(mainJobsDTOList);

        return mainJobsDTOResponseHandler ;
    }

    public Date ConvetMilliSecToDate(Long milliSec){
        return new Date(milliSec);
    }
}

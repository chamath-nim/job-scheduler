package com.mobitel.servicereq.service;

import com.mobitel.servicereq.job.JobOne;
import com.mobitel.servicereq.job.JobStatus;
import com.mobitel.servicereq.job.JobThree;
import com.mobitel.servicereq.job.JobTwo;
import com.mobitel.servicereq.model.JobDetails;
import com.mobitel.servicereq.model.MainJobs;
import com.mobitel.servicereq.model.RecJobDetails;
import com.mobitel.servicereq.model.SR;
import com.mobitel.servicereq.repo.FiredJobsRepo;
import com.mobitel.servicereq.repo.RecJobRepo;
import com.mobitel.servicereq.repo.SRRepo;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;
import java.util.UUID;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
@Slf4j
public class SRService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private SRRepo srRepo;

    @Autowired
    private FiredJobsRepo firedJobsRepo;

    @Autowired
    private Scheduler schedulerST;


    Logger logger = LoggerFactory.getLogger(SRService.class);

    public void mainTask(){
        job1();
        job2();
        job3();

        jobStatus();
    }

    public void jobStatus(){
        try {
            JobDetail job = newJob(JobStatus.class)
                    .withIdentity(UUID.randomUUID().toString(), "jobST")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(job.getKey().getName(), "jobST")
                    .withSchedule(cronSchedule(" */30 * * * * ?"))
                    .forJob(job)
                    .build();

            schedulerST.scheduleJob(job,trigger);
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void job1() {
        try {
            JobDetail job = newJob(JobOne.class)
                    .withIdentity(UUID.randomUUID().toString(), "group1")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(job.getKey().getName(), "group1")
                    .withDescription("sample1")
                    .withSchedule(cronSchedule(" */30 * * * * ?"))
                    .forJob(job)
                    .build();

            scheduler.scheduleJob(job,trigger);
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void job2() {
        try {
            JobDetail job = newJob(JobTwo.class)
                    .withIdentity(UUID.randomUUID().toString(), "group2")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(job.getKey().getName(), "group2")
                    .withDescription("sample2")
                    .withSchedule(cronSchedule(" */30 * * * * ?"))
                    .forJob(job)
                    .build();

            scheduler.scheduleJob(job,trigger);
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void job3() {
        try {
            JobDetail job = newJob(JobThree.class)
                    .withIdentity(UUID.randomUUID().toString(), "group3")
                    .build();

            Trigger trigger = newTrigger()
                    .withIdentity(job.getKey().getName(), "group3")
                    .withDescription("sample3")
                    .withSchedule(cronSchedule(" */30 * * * * ?"))
                    .forJob(job)
                    .build();

            scheduler.scheduleJob(job,trigger);
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void deleteJob(String jobGroup){
        try {
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup))) {
                System.out.println(jobKey.getName());
                scheduler.deleteJob(jobKey);
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void pauseTrigger(String triggerGroup){
        try {
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup))) {
                System.out.println(triggerKey.getName());
                scheduler.pauseTrigger(triggerKey);
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void resumeTrigger(String triggerGroup){
        try {
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroup))) {
                System.out.println(triggerKey.getName());
                scheduler.resumeTrigger(triggerKey);
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void deleteAllJobs(){
        try {
            List<JobKey> jobKeyList = new ArrayList<>();
            for (String groupName : scheduler.getJobGroupNames()) {
                jobKeyList.addAll(scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName)));
            }
            scheduler.deleteJobs(jobKeyList);
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public void addRequst(SR sr){
        sr.setRequestTime(CurrentDateTime());
        srRepo.save(sr);
    }

    public List<JobDetails> getFiredJobs(int count){
        return firedJobsRepo.getFiredJobs(count);
    }

    public List<MainJobs> getJobs(){
        List<MainJobs> jobs = new ArrayList<>();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                if (!groupName.equals("jobST")) {
                    for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
                        MainJobs mainJobs = new MainJobs();

                        mainJobs.setTiggerName(triggerKey.getName());
                        mainJobs.setTriggerGroup(triggerKey.getGroup());
                        mainJobs.setTriggerState(String.valueOf(scheduler.getTriggerState(triggerKey)));
                        mainJobs.setDescription(scheduler.getTrigger(triggerKey).getDescription());

                        jobs.add(mainJobs);
                    }
                }
            }
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
        return jobs;
    }

    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}

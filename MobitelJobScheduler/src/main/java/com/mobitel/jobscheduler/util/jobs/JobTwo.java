package com.mobitel.jobscheduler.util.jobs;

import com.mobitel.jobscheduler.domain.FiredJobs;
import com.mobitel.jobscheduler.domain.ServiceRequests;
import com.mobitel.jobscheduler.repository.FiredJobsRepo;
import com.mobitel.jobscheduler.repository.ServiceRequestRepo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JobTwo extends QuartzJobBean {

    @Autowired
    private ServiceRequestRepo serviceRequestRepo;

    @Autowired
    private FiredJobsRepo firedJobsRepo;

    @Autowired
    private Scheduler scheduler;

    Logger logger = LoggerFactory.getLogger(JobTwo.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<ServiceRequests> requests = new ArrayList<>();

        String classNameJobOne = "class com.mobitel.jobscheduler.util.jobs.JobOne";
        Trigger.TriggerState state = Trigger.TriggerState.NORMAL;
        boolean status = false;

        try {
            for (String groupName : scheduler.getTriggerGroupNames()){
                for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {

                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    String name = String.valueOf(scheduler.getJobDetail(trigger.getJobKey()).getJobClass());
                    if (classNameJobOne.equals(name) && state.equals(scheduler.getTriggerState(triggerKey))){
                        status = true;
                        break;
                    }
                }
            }
            if(status){
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount1(1, CurrentDateTime()));
            }
            else{
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount1(0, CurrentDateTime()));
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount1(1, CurrentDateTime()));

            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }


        if (requests.size() == 0) logger.info("There is no in-progress requests in for job 2");
        else {
            System.out.println("reqsize "+requests.size());
            for (ServiceRequests sr : requests) {
                LocalDateTime dateTime = sr.getSr_CREATED_ON();

                LocalDateTime nextDateTime = dateTime.plusMinutes(2);

                try {
                    FiredJobs firedJobs = new FiredJobs();
                    firedJobs.setServiceRequestId(sr.getID());
                    firedJobs.setStartTime(nextDateTime);
                    firedJobs.setActualStartTime(CurrentDateTime());

                    logger.info("job2 " + sr.getID());

                    sr.setNOTIFY_COUNT(2);
                    serviceRequestRepo.save(sr);

                    firedJobs.setStatus("Success");
                    firedJobs.setEndTime(CurrentDateTime());

                    firedJobsRepo.save(firedJobs);
                }
                catch (NullPointerException e){
                    logger.error(String.valueOf(e));
                }
            }
        }
    }

    public LocalDateTime CurrentDateTime(){
        return LocalDateTime.now();
    }
}

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
public class JobThree extends QuartzJobBean{

    @Autowired
    private ServiceRequestRepo serviceRequestRepo;

    @Autowired
    private FiredJobsRepo firedJobsRepo;

    @Autowired
    private Scheduler scheduler;

    Logger logger = LoggerFactory.getLogger(JobThree.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        List<ServiceRequests> requests = new ArrayList<>();

        String classNameJobOne = "class com.mobitel.jobscheduler.util.jobs.JobOne";
        String classNameJobTwo = "class com.mobitel.jobscheduler.util.jobs.JobTwo";

        Trigger.TriggerState state = Trigger.TriggerState.NORMAL;
        boolean status1 = false;
        boolean status2 = false;

        try {
            for (String groupName : scheduler.getTriggerGroupNames()){
                for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {

                    Trigger trigger = scheduler.getTrigger(triggerKey);
                    String name = String.valueOf(scheduler.getJobDetail(trigger.getJobKey()).getJobClass());
                    if (classNameJobOne.equals(name) && state.equals(scheduler.getTriggerState(triggerKey))){
                        status1 = true;
                    }
                    if (classNameJobTwo.equals(name) && state.equals(scheduler.getTriggerState(triggerKey))){
                        status2 = true;
                    }
                }
            }
            if(status1 && status2){
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount2(2, CurrentDateTime()));
            }
            else if (status1){
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount2(1, CurrentDateTime()));
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount2(2, CurrentDateTime()));
            }
            else{
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount2(0, CurrentDateTime()));
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount2(1, CurrentDateTime()));
                requests.addAll(serviceRequestRepo.findEligibleServiceRequestsCount2(2, CurrentDateTime()));

            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }


        if (requests.size() == 0) logger.info("There is no in-progress requests in for job 3");
        else {
            for (ServiceRequests sr : requests) {
                LocalDateTime dateTime = sr.getSr_CREATED_ON();

                LocalDateTime nextDateTime = dateTime.plusMinutes(5);

                try {
                    FiredJobs firedJobs = new FiredJobs();
                    firedJobs.setServiceRequestId(sr.getID());
                    firedJobs.setStartTime(nextDateTime);
                    firedJobs.setActualStartTime(CurrentDateTime());

                    logger.info("job3 " + sr.getID());
                    sr.setNOTIFY_COUNT(3);
                    serviceRequestRepo.save(sr);

                    firedJobs.setStatus("Success");
                    //job process time
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

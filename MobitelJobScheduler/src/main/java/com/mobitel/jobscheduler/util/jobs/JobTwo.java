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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

        try {
            Trigger.TriggerState state = Trigger.TriggerState.NORMAL;
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals("group1"))) {
                state = scheduler.getTriggerState(triggerKey);
            }
            if (state.equals(Trigger.TriggerState.PAUSED)){
                List<ServiceRequests> req1 = serviceRequestRepo.findEligibleServiceRequests(0);
                List<ServiceRequests> req2 = serviceRequestRepo.findEligibleServiceRequests(1);

                requests.addAll(req1);
                requests.addAll(req2);
            }
            else requests = serviceRequestRepo.findEligibleServiceRequests(1);

        } catch (SchedulerException e) {
            logger.error(String.valueOf(e));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.systemDefault();

        if (requests.size() == 0) logger.info("There is no in-progress requests in for job 2");
        else {
            System.out.println("reqsize "+requests.size());
            for (ServiceRequests sr : requests) {
                LocalDateTime dateTime = sr.getSr_CREATED_ON();

                LocalDateTime nextDateTime = dateTime.plusMinutes(3);
                ZonedDateTime zonedDateTime = nextDateTime.atZone(zoneId);

                ZonedDateTime checking = ZonedDateTime.now();

                if (checking.compareTo(zonedDateTime) > 0) {

                    FiredJobs jobDetails = firedJobsRepo.getByJobId(sr.getID(),2);
                    jobDetails.setActualStartTime(CurrentDateTime());

                    logger.info("job2 "+sr.getID());
                    sr.setNOTIFY_COUNT(2);
                    serviceRequestRepo.save(sr);

                    jobDetails.setStatus("Success");
                    jobDetails.setEndTime(CurrentDateTime());

                    firedJobsRepo.save(jobDetails);
                }
                else {
                    if(firedJobsRepo.checkExist(sr.getID(), 2)==0) {
                        FiredJobs firedJobs = new FiredJobs();
                        firedJobs.setServiceRequestId(sr.getID());
                        firedJobs.setNotifyCount(2);
                        firedJobs.setStartTime(nextDateTime.format(formatter));
                        firedJobs.setStatus("Queued");

                        firedJobsRepo.save(firedJobs);
                    }
                }
            }
        }
    }
    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}

package com.mobitel.jobscheduler.util.jobs;

import com.mobitel.jobscheduler.domain.FiredJobs;
import com.mobitel.jobscheduler.domain.ServiceRequests;
import com.mobitel.jobscheduler.repository.FiredJobsRepo;
import com.mobitel.jobscheduler.repository.ServiceRequestRepo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Component
public class JobOne extends QuartzJobBean {

    @Autowired
    private ServiceRequestRepo serviceRequestRepo;

    @Autowired
    private FiredJobsRepo firedJobsRepo;

    Logger logger = LoggerFactory.getLogger(JobOne.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {

        List<ServiceRequests> requests = serviceRequestRepo.findEligibleServiceRequests(0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (requests.size() == 0) logger.info("There is no in-progress requests in for job 1");
        else {
            for (ServiceRequests sr : requests) {

                logger.info("job1 "+sr.getId());
                sr.setNotifyCount(1);
                serviceRequestRepo.save(sr);

                FiredJobs firedJobs = new FiredJobs();
                firedJobs.setServiceRequestId(sr.getId());
                firedJobs.setNotifyCount(1);
                firedJobs.setStartTime(CurrentDateTime());
                firedJobs.setStatus("Success");
                firedJobs.setActualStartTime(CurrentDateTime());
                firedJobs.setEndTime(CurrentDateTime());

                firedJobsRepo.save(firedJobs);
            }
        }
    }
    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}

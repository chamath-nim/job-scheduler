package com.mobitel.servicereq.job;

import com.mobitel.servicereq.model.RecJobDetails;
import com.mobitel.servicereq.repo.RecJobRepo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class JobStatus extends QuartzJobBean {

    @Autowired
    private RecJobRepo recJobRepo;

    @Autowired
    private Scheduler scheduler;

    Logger logger = LoggerFactory.getLogger(JobOne.class);

    @Override
    protected void executeInternal(JobExecutionContext context) {
        RecJobDetails recJobDetails = new RecJobDetails();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                switch (groupName) {
                    case "group1" -> recJobDetails.setJob1Status(getST(groupName));
                    case "group2" -> recJobDetails.setJob2Status(getST(groupName));
                    case "group3" -> recJobDetails.setJob3Status(getST(groupName));
                }
            }
            recJobDetails.setFiredTime(CurrentDateTime());
            recJobRepo.save(recJobDetails);
        }
        catch (SchedulerException e){
            logger.error(String.valueOf(e));
        }
    }

    public String getST(String groupName) throws SchedulerException {
        Trigger.TriggerState state = null;
        for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
            state = scheduler.getTriggerState(triggerKey);

        }
        assert state != null;
        return state.toString();
    }

    public String CurrentDateTime(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return format.format(now);
    }
}

package com.mobitel.jobscheduler.service;

import com.mobitel.jobscheduler.domain.MainJobs;
import com.mobitel.jobscheduler.dto.MainJobsDTO;
import com.mobitel.jobscheduler.util.generic.RequestHandler;
import com.mobitel.jobscheduler.util.generic.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.quartz.*;
import org.quartz.impl.calendar.BaseCalendar;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Calendar;

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

    ZoneId z = ZoneId.systemDefault();

    public Trigger mainTrigger(String name, String group, String description, String cronExpression,
                               JobDetail jobDetail, LocalDateTime startTime, LocalDateTime endTime,
                               int repetitions) {

        Date time = convertToDate(Objects.requireNonNullElseGet(startTime, LocalDateTime::now));

        Trigger trigger = newTrigger()
                .withIdentity(name, group)
                .withDescription(description)
                .withSchedule(cronSchedule(cronExpression))
                .startAt(Date.from(time.toInstant()))
                .forJob(jobDetail)
                .build();

        if ((startTime != null) && (endTime != null) && (repetitions == 0)){
            return trigger.getTriggerBuilder().startAt(convertToDate(startTime)).endAt(convertToDate(endTime)).build();
        }

        else if (startTime != null && endTime == null) {
            if (repetitions > 0) {
                return trigger.getTriggerBuilder().endAt(getEndDate(trigger,repetitions)).build();
            }
            else return trigger;
        }

        else if (endTime != null) return trigger.getTriggerBuilder().endAt(convertToDate(endTime)).build();

        else if (repetitions > 0) return trigger.getTriggerBuilder().endAt(getEndDate(trigger,repetitions)).build();

        else return trigger;
    }

    public ResponseHandler<String> mainScheduler(RequestHandler<MainJobsDTO> jobDTORequestHandler){
        ResponseHandler<String> jobDTOResponseHandler = new ResponseHandler<>();

        try {
            MainJobs job = modelMapper.map(jobDTORequestHandler.getBody(), MainJobs.class);

            JobDetail jobDetail = newJob(Class.forName(job.getJobClassName()).asSubclass(Job.class))
                    .withIdentity(job.getJobName(),job.getJobGroup())
                    .build();

            LocalDateTime startTime = job.getScheduledStart();
            LocalDateTime endTime = job.getScheduledEnd();
            boolean status = false;

            if ((startTime != null) && (endTime != null)) {
               if (startTime.isBefore(LocalDateTime.now()) && endTime.isBefore(startTime)) {
                   jobDTOResponseHandler.setBody("Scheduled start time must be after current time and"
                                                +"Scheduled end time must be after start time.");
               }
               else if (endTime.isBefore(startTime)){
                    jobDTOResponseHandler.setBody("Scheduled end time must be after start time or current time. ");
               }
               else if (startTime.isBefore(LocalDateTime.now())){
                   jobDTOResponseHandler.setBody("Scheduled start time must be after current time. ");
               }
               else status = true;

            }
            else if (startTime != null){
                if (startTime.isBefore(LocalDateTime.now())) {
                    jobDTOResponseHandler.setBody("Scheduled start time must be after current time.");
                }
                else status = true;

            }
            else if (endTime != null){
                if (endTime.isBefore(LocalDateTime.now())) {
                    jobDTOResponseHandler.setBody("Scheduled end time must be after current time.");
                }
                else status = true;

            }
            else status = true;

            if (status){
                Trigger trigger = mainTrigger(job.getTriggerName(), job.getTriggerGroup(), job.getTriggerDescription(),
                                          job.getCronExpression(), jobDetail, startTime, endTime, job.getRepetitions());

                scheduler.scheduleJob(jobDetail, trigger);
                jobDTOResponseHandler.setBody("Successfully created scheduled the job.");
            }
        }
        catch (SecurityException | SchedulerException | ClassNotFoundException | NullPointerException | IllegalArgumentException e){
            logger.error(String.valueOf(e));
        }
        return jobDTOResponseHandler;
    }

    public Date convertToDate(LocalDateTime localDateTime){
        return Date.from(ZonedDateTime.of(localDateTime, z).toInstant());
    }

    public Date getEndDate(Trigger trigger, int repetitions){
        return TriggerUtils.computeEndTimeToAllowParticularNumberOfFirings((OperableTrigger) trigger,
                new BaseCalendar(Calendar.getInstance().getTimeZone()), repetitions);
    }
}

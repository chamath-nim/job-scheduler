package com.mobitel.jobscheduler.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "fired_jobs")
@DynamicUpdate
public class FiredJobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "FIRED_JOB_ID")
    private Long firedJobId;

    @Column(nullable = false, name = "SERVICE_REQUEST_ID")
    private Long ServiceRequestId;

    @Column(nullable = false, name = "STATUS")
    private String status;

    @Column(name = "ACTUAL_START_TIME")
    private LocalDateTime actualStartTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Column(name = "START_TIME")
    private LocalDateTime startTime;

    @Column(name = "NOTIFY_COUNT", nullable = false)
    private int notifyCount;
}

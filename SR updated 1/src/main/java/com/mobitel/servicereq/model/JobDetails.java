package com.mobitel.servicereq.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "fired_jobs")
@DynamicUpdate
public class JobDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, name = "JOB_ID")
    private Long JobId;

    @Column(nullable = false, name = "STATUS")
    private String status;

    @Column(name = "ACTUAL_START_TIME")
    private String actualStartTime;

    @Column(name = "END_TIME")
    private String endTime;

    @Column(name = "START_TIME", nullable = false)
    private String startTime;

    @Column(name = "NOTIFY_COUNT", nullable = false)
    private int notifyCount;



}

package com.mobitel.jobscheduler.domain;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Data
@Table(name = "fired_jobs")
@DynamicUpdate
public class ServiceRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "SERVICE_REQUEST_ID")
    private Long id;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "SUB_STATUS", nullable = false)
    private String subStatus;

    @Column(name = "REQUEST_TIME", nullable = false)
    private String requestTime;

    @Column(name = "NOTIFY_COUNT", nullable = false)
    private int notifyCount;
}

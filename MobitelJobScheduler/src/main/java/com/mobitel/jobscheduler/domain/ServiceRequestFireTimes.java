package com.mobitel.jobscheduler.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "sr_fire_times")
@DynamicUpdate
public class ServiceRequestFireTimes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "FIRE_TIME_ID")
    private Long fireTimeId;

    @Column(nullable = false, name = "SERVICE_REQUEST_ID")
    private Long ServiceRequestId;

    @Column(name = "JOB_ONE_FIRE_TIME")
    private LocalDateTime jobOneFireTime;

    @Column(name = "JOB_TWO_FIRE_TIME")
    private LocalDateTime jobTwoFireTime;

    @Column(name = "JOB_THREE_FIRE_TIME")
    private LocalDateTime jobThreeFireTime;
}

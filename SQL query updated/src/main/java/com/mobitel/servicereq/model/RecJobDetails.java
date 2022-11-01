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
@Table(name = "rec_fired_jobs")
@DynamicUpdate
public class RecJobDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, name = "JOB1_ST")
    private String job1Status;

    @Column(nullable = false, name = "JOB2_ST")
    private String job2Status;

    @Column(nullable = false, name = "JOB3_ST")
    private String job3Status;

    @Column(name = "FIRED_TIME", nullable = false)
    private String firedTime;

}

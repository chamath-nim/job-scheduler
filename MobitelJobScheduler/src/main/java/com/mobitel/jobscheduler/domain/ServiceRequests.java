package com.mobitel.jobscheduler.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "service_requests")
@DynamicUpdate
public class ServiceRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long ID;

    @Column(name = "NOTIFY_COUNT")
    private int NOTIFY_COUNT;

    @Column(name = "SR_AREA")
    private String sr_AREA;

//    @Column(name = "SR_ASSIGN_NOTIFY")
//    private int SR_ASSIGN_NOTIFY;
//
//    @Column(name = "SR_CAT_TYPE")
//    private String SR_CAT_TYPE;

    @Column(name = "SR_CLOSED_BY")
    private String sr_CLOSED_BY;

    @Column(name = "SR_CLOSED_ON")
    private String sr_CLOSED_ON;

//    @Column(name = "SR_CLOSING_COMMENT")
//    private String SR_CLOSING_COMMENT;

    @Column(name = "SR_CREATED_BY")
    private String sr_CREATED_BY;

    @Column(name = "SR_CREATED_ON")
    private String sr_CREATED_ON;

//    @Column(name = "SR_CUST_NOTIFY_CLOSE")
//    private int SR_CUST_NOTIFY_CLOSE;
//
//    @Column(name = "SR_CUST_NOTIFY_OPEN")
//    private int SR_CUST_NOTIFY_OPEN;
//
//    @Column(name = "SR_FIELD1")
//    private String SR_FIELD1;
//
//    @Column(name = "SR_FIELD2")
//    private String SR_FIELD2;
//
//    @Column(name = "SR_FIELD3")
//    private String SR_FIELD3;
//
//    @Column(name = "SR_FIELD4")
//    private String SR_FIELD4;
//
//    @Column(name = "SR_FIELD5")
//    private String SR_FIELD5;
//
//    @Column(name = "SR_FIELD6")
//    private Long SR_FIELD6;
//
//    @Column(name = "SR_FTR")
//    private int SR_FTR;
//
//    @Column(name = "SR_LAST_UPDATED_BY")
//    private String SR_LAST_UPDATED_BY;
//
//    @Column(name = "SR_LAST_UPDATED_ON")
//    private String SR_LAST_UPDATED_ON;
//
    @Column(name = "SR_MOBILE_NO")
    private String sr_MOBILE_NO;

//    @Column(name = "SR_NON_FTR")
//    private int SR_NON_FTR;

    @Column(name = "SR_NOTIFICATION_EMAIL")
    private String sr_NOTIFICATION_EMAIL;

//    @Column(name = "SR_NOTIFICATION_EMAIL_FLAG")
//    private int SR_NOTIFICATION_EMAIL_FLAG;
//
//    @Column(name = "SR_NOTIFICATION_MOBILE_NO")
//    private String SR_NOTIFICATION_MOBILE_NO;

//    @Column(name = "SR_NUM")
//    private String SR_NUM;
//
//    @Column(name = "SR_OPENING_COMMENT")
//    private String SR_OPENING_COMMENT;

    @Column(name = "SR_OWNER")
    private String sr_OWNER;

    @Column(name = "SR_OWNER_GROUP")
    private String sr_OWNER_GROUP;

//    @Column(name = "SR_PRIORITY")
//    private String SR_PRIORITY;
//
//    @Column(name = "SR_PURGED_DATE")
//    private String SR_PURGED_DATE;
//
//    @Column(name = "SR_READY_TO_PURGE")
//    private int SR_READY_TO_PURGE;
//
//    @Column(name = "SR_SEVERITY")
//    private String SR_SEVERITY;
//
//    @Column(name = "SR_SOURCE")
//    private String SR_SOURCE;

    @Column(name = "SR_STATUS")
    private String sr_STATUS;

//    @Column(name = "SR_SUB_AREA")
//    private String SR_SUB_AREA;

    @Column(name = "SR_SUB_STATUS")
    private String sr_SUB_STATUS;

    @Column(name = "SR_COMMITED_DATE")
    private String sr_COMMITED_DATE;

//    @Column(name = "SR_WEIGHTAGE_FACTOR")
//    private String SR_WEIGHTAGE_FACTOR;


}

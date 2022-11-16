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
    private Long id;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "SUB_STATUS", nullable = false)
    private String subStatus;

    @Column(name = "REQUEST_TIME", nullable = false)
    private String requestTime;

    @Column(name = "NOTIFY_COUNT", nullable = false)
    private int notifyCount;

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(nullable = false, updatable = false)
//    private Long ID;
//    private int NOTIFY_COUNT;
//    private String SR_AREA;
//    private int SR_ASSIGN_NOTIFY;
//    private String SR_CAT_TYPE;
//    private String SR_CLOSED_BY;
//    private String SR_CLOSED_ON;
//    private String SR_CLOSING_COMMENT;
//    private String SR_CREATED_BY;
//    private String SR_CREATED_ON;
//    private int SR_CUST_NOTIFY_CLOSE;
//    private int SR_CUST_NOTIFY_OPEN;
////    private String SR_FIELD1;
////    private String SR_FIELD2;
////    private String SR_FIELD3;
////    private String SR_FIELD4;
////    private String SR_FIELD5;
////    private Long SR_FIELD6;
////    private int SR_FTR;
//    private String SR_LAST_UPDATED_BY;
//    private String SR_LAST_UPDATED_ON;
//    private String SR_MOBILE_NO;
//    private int SR_NON_FTR;
//    private String SR_NOTIFICATION_EMAIL;
//    private int SR_NOTIFICATION_EMAIL_FLAG;
//    private String SR_NOTIFICATION_MOBILE_NO;
//    private String SR_NUM;
//    private String SR_OPENING_COMMENT;
//    private String SR_OWNER;
//    private String SR_OWNER_GROUP;
//    private String SR_PRIORITY;
//    private String SR_PURGED_DATE;
//    private int SR_READY_TO_PURGE;
//    private String SR_SEVERITY;
//    private String SR_SOURCE;
//    private String SR_STATUS;
//    private String SR_SUB_AREA;
//    private String SR_SUB_STATUS;
//    private String SR_WEIGHTAGE_FACTOR;

}

package com.mobitel.jobscheduler.repository;

import com.mobitel.jobscheduler.domain.ServiceRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceRequestRepo extends JpaRepository<ServiceRequests, Long> {

    @Query("select s from ServiceRequests s left join ServiceRequestFireTimes f on s.ID = f.ServiceRequestId " +
            "where (s.sr_STATUS = 'inprogress' and s.NOTIFY_COUNT = ?1 and f.jobOneFireTime < ?2)")
    List<ServiceRequests> findEligibleServiceRequestsCount0(int i, LocalDateTime j);

    @Query("select s from ServiceRequests s left join ServiceRequestFireTimes f on s.ID = f.ServiceRequestId " +
            "where (s.sr_STATUS = 'inprogress' and s.NOTIFY_COUNT = ?1 and f.jobTwoFireTime < ?2)")
    List<ServiceRequests> findEligibleServiceRequestsCount1(int i, LocalDateTime j);

    @Query("select s from ServiceRequests s left join ServiceRequestFireTimes f on s.ID = f.ServiceRequestId " +
            "where (s.sr_STATUS = 'inprogress' and s.NOTIFY_COUNT = ?1 and f.jobThreeFireTime < ?2)")
    List<ServiceRequests> findEligibleServiceRequestsCount2(int i, LocalDateTime j);

    @Query("select s from ServiceRequests s where (s.sr_STATUS = 'inprogress' and s.NOTIFY_COUNT = ?1)")
    List<ServiceRequests> findEligibleServiceRequests(int i);
}

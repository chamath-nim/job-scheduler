package com.mobitel.jobscheduler.repository;

import com.mobitel.jobscheduler.domain.ServiceRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRequestRepo extends JpaRepository<ServiceRequests, Long> {

    @Query("select s from ServiceRequests s where (s.SR_STATUS = 'inprogress' and s.NOTIFY_COUNT = ?1)")
    List<ServiceRequests> findEligibleServiceRequests(int i);
}

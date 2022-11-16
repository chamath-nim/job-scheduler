package com.mobitel.jobscheduler.repository;

import com.mobitel.jobscheduler.domain.FiredJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FiredJobsRepo extends JpaRepository<FiredJobs, Long> {

    @Query("select f from FiredJobs f where f.notifyCount = ?1")
    List<FiredJobs> getFiredJobs(int i);

    @Query("select j from FiredJobs j where j.ServiceRequestId = ?1 and j.notifyCount = ?2")
    FiredJobs getByJobId(Long i, int j);

    @Query("select count(j) from FiredJobs j where j.ServiceRequestId = ?1 and j.notifyCount = ?2")
    int checkExist(Long i, int j);
}
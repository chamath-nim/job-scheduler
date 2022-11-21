package com.mobitel.jobscheduler.repository;

import com.mobitel.jobscheduler.domain.ServiceRequestFireTimes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRequestFireTimesRepo extends JpaRepository<ServiceRequestFireTimes, Long> {
}

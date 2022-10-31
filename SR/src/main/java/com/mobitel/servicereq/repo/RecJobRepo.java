package com.mobitel.servicereq.repo;

import com.mobitel.servicereq.model.RecJobDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecJobRepo extends JpaRepository<RecJobDetails, Long> {
}

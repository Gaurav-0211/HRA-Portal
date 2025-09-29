package com.hra.hra.repository;

import com.hra.hra.config.LeaveType;
import com.hra.hra.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployeeId(Long id);

}

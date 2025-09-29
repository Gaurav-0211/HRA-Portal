package com.hra.hra.config;

import com.hra.hra.service.impl.LeaveServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LeaveAccrualScheduler {
    private final LeaveServiceImplementation leaveService;

    // Run at 00:00 on day 1 of every month
    @Scheduled(cron = "0 0 0 1 * ?")
    public void runMonthlyAccrualScheduler() {
        this.leaveService.runMonthlyAccrual(LocalDate.now());
    }
}

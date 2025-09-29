package com.hra.hra.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "leave.policy")
@Getter
@Setter
public class LeavePolicyProperties {
    // annual allocations
    private BigDecimal clAnnual = BigDecimal.valueOf(7);
    private BigDecimal slAnnual = BigDecimal.valueOf(7);
    private BigDecimal elAnnual = BigDecimal.valueOf(18);

    // monthly cap (global)
    private BigDecimal monthlyPaidCap = BigDecimal.valueOf(2.5);

    // Annual allocation for each type
    public BigDecimal getAnnualAllocationForType(LeaveType type) {
        switch (type) {
            case CASUAL:
                return clAnnual;
            case SICK:
                return slAnnual;
            case EARNED:
                return elAnnual;
            default:
                return BigDecimal.ZERO;
        }
    }

    // Monthly accrual
    public BigDecimal getMonthlyAccrualForType(LeaveType type) {
        BigDecimal months = BigDecimal.valueOf(12);
        switch (type) {
            case CASUAL:
                return clAnnual.divide(months, 6, java.math.RoundingMode.HALF_UP);
            case SICK:
                return slAnnual.divide(months, 6, java.math.RoundingMode.HALF_UP);
            case EARNED:
                return elAnnual.divide(months, 6, java.math.RoundingMode.HALF_UP);
            default:
                return BigDecimal.ZERO;
        }
    }
}

package com.hra.hra.repository;

import com.hra.hra.entity.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.token = :token")
    void deleteByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.employee.id = :employeeId")
    void deleteByEmployeeId(Long employeeId);

}

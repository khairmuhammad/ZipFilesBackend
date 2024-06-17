package com.khair.zipfilesbackend.repository;

import com.khair.zipfilesbackend.model.UploadStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UploadStatsRepository extends JpaRepository<UploadStats, Long> {
    Optional<UploadStats> findByIpAndDate(String ip, LocalDate date);
}
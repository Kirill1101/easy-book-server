package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.ScheduleDate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDateRepository extends JpaRepository<ScheduleDate, UUID> {
  List<ScheduleDate> findScheduleDatesBySchedule_Id(UUID scheduleDateId);

  Optional<ScheduleDate> findScheduleDateBySchedule_IdAndDate(UUID scheduleId, LocalDate date);
}

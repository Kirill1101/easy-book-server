package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.ScheduleDate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleDateRepository extends JpaRepository<ScheduleDate, Long> {
  List<ScheduleDate> findScheduleDatesBySchedule_Id(Long scheduleDateId);

  Optional<ScheduleDate> findScheduleDateBySchedule_IdAndDate(Long scheduleId, LocalDate date);
}

package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
  List<Schedule> getSchedulesByUserCreatorId(Long userId);
}

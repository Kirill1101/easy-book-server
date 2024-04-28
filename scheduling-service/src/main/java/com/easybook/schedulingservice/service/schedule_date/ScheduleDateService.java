package com.easybook.schedulingservice.service.schedule_date;

import com.easybook.schedulingservice.entity.ScheduleDate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleDateService {
  ScheduleDate createScheduleDate(ScheduleDate scheduleDate);

  Optional<ScheduleDate> getScheduleDateById(UUID scheduleDateId);

  List<ScheduleDate> getAllScheduleDatesByScheduleId(UUID scheduleId);

  ScheduleDate updateScheduleDate(ScheduleDate scheduleDate);

  void deleteScheduleDateById(UUID scheduleDateId);
}

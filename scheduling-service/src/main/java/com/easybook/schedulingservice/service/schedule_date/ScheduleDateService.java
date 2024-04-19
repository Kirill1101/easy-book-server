package com.easybook.schedulingservice.service.schedule_date;

import com.easybook.schedulingservice.entity.ScheduleDate;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleDateService {
  ScheduleDate createScheduleDate(ScheduleDate scheduleDate);

  Optional<ScheduleDate> getScheduleDateById(Long scheduleDateId);

  List<ScheduleDate> getAllScheduleDatesByScheduleId(Long scheduleId);

  ScheduleDate updateScheduleDate(ScheduleDate scheduleDate);

  void deleteScheduleDateById(Long scheduleDateId);
}

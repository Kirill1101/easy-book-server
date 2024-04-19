package com.easybook.schedulingservice.service.schedule;

import com.easybook.schedulingservice.entity.Schedule;
import java.util.List;
import java.util.Optional;

public interface ScheduleService {
  Schedule createSchedule(Schedule schedule);

  Optional<Schedule> getScheduleById(Long id);

  List<Schedule> getSchedulesByUserId(Long userId);

  Schedule updateSchedule(Schedule schedule);

  void deleteScheduleById(Long id);

  boolean scheduleIsExists(Long id);
}

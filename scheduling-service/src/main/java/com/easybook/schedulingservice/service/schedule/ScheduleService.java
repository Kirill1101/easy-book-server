package com.easybook.schedulingservice.service.schedule;

import com.easybook.schedulingservice.entity.Schedule;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleService {
  Schedule createSchedule(Schedule schedule);

  Optional<Schedule> getScheduleById(UUID id);

  List<Schedule> getSchedulesByUserId(UUID userId);

  Schedule updateSchedule(Schedule schedule);

  void deleteScheduleById(UUID id);

  boolean scheduleIsExists(UUID id);
}

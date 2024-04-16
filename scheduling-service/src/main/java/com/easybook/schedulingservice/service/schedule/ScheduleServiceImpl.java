package com.easybook.schedulingservice.service.schedule;

import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.repository.ScheduleRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  ScheduleRepository scheduleRepository;
  @Override
  public Schedule createSchedule(Schedule schedule) {
    return scheduleRepository.save(schedule);
  }

  @Override
  public Optional<Schedule> getScheduleById(Long id) {
    return scheduleRepository.findById(id);
  }

  @Override
  public List<Schedule> getSchedulesByUserId(Long userId) {
    return scheduleRepository.getSchedulesByUserCreatorId(userId);
  }

  @Override
  public Schedule updateSchedule(Schedule schedule) {
    return scheduleRepository.save(schedule);
  }

  @Override
  public void deleteScheduleById(Long id) {
    scheduleRepository.deleteById(id);
  }
}

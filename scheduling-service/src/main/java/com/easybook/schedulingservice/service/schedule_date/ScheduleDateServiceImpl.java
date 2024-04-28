package com.easybook.schedulingservice.service.schedule_date;

import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.repository.ScheduleDateRepository;
import com.easybook.schedulingservice.service.slot.SlotService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ScheduleDateServiceImpl implements ScheduleDateService{

  private final SlotService slotService;

  private final ScheduleDateRepository scheduleDateRepository;

  @Override
  public ScheduleDate createScheduleDate(ScheduleDate scheduleDate) {
    scheduleDate.getSlots().forEach(slot -> slot.setScheduleDate(scheduleDate));
    return scheduleDateRepository.save(scheduleDate);
  }

  @Override
  public Optional<ScheduleDate> getScheduleDateById(UUID scheduleDateId) {
    return scheduleDateRepository.findById(scheduleDateId);
  }

  @Override
  public List<ScheduleDate> getAllScheduleDatesByScheduleId(UUID scheduleId) {
    return scheduleDateRepository.findScheduleDatesBySchedule_Id(scheduleId);
  }

  @Override
  public ScheduleDate updateScheduleDate(ScheduleDate scheduleDate) {
    ScheduleDate scheduleDateFromBase = getScheduleDateById(scheduleDate.getId()).orElseThrow();
    if (scheduleDate.getDate() != null) {
      scheduleDateFromBase.setDate(scheduleDate.getDate());
    }
    if (scheduleDate.getSlots() != null) {
      scheduleDate.getSlots().forEach(slot -> slot.setScheduleDate(scheduleDate));
      scheduleDate.getSlots().forEach(slotService::updateSlot);
      scheduleDateFromBase.setSlots(scheduleDate.getSlots());
    }
    return scheduleDateRepository.save(scheduleDateFromBase);
  }

  @Override
  public void deleteScheduleDateById(UUID scheduleDateId) {
    scheduleDateRepository.deleteById(scheduleDateId);
  }
}

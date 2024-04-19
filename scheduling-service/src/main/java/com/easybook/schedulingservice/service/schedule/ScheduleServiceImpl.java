package com.easybook.schedulingservice.service.schedule;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.repository.ScheduleRepository;
import com.easybook.schedulingservice.service.appointment.AppointmentService;
import com.easybook.schedulingservice.service.schedule_date.ScheduleDateService;
import com.easybook.schedulingservice.service.service.ServiceService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

  private final ScheduleDateService scheduleDateService;

  private final ServiceService serviceService;

  private final AppointmentService appointmentService;

  private final ScheduleRepository scheduleRepository;

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
  @Transactional
  public Schedule updateSchedule(Schedule schedule) {
    Schedule scheduleFromBase = getScheduleById(schedule.getId()).orElseThrow();

    if (schedule.getServices() != null) {
      schedule.getServices().forEach(service -> service.setSchedule(schedule));
      scheduleFromBase.setServices(schedule.getServices());
    }

    if (schedule.getAvailableDates() != null) {
      schedule.getAvailableDates().forEach(date -> {
        date.setSchedule(schedule);
        date.getSlots().forEach(slot -> slot.setScheduleDate(date));
      });
      scheduleFromBase.setAvailableDates(schedule.getAvailableDates());
    }

    if (schedule.getAppointments() != null) {
      schedule.getAppointments().forEach(appointment -> appointment.setSchedule(schedule));
      scheduleFromBase.setAppointments(schedule.getAppointments());
    }

    return scheduleRepository.save(scheduleFromBase);
  }

  @Override
  public void deleteScheduleById(Long id) {
    scheduleRepository.deleteById(id);
  }

  @Override
  public boolean scheduleIsExists(Long id) {
    return scheduleRepository.existsById(id);
  }
}

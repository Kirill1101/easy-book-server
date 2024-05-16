package com.easybook.schedulingservice.service;

import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.repository.ScheduleRepository;
import com.easybook.schedulingservice.service.appointment.AppointmentService;
import com.easybook.schedulingservice.service.schedule.ScheduleServiceImpl;
import com.easybook.schedulingservice.service.schedule_date.ScheduleDateService;
import com.easybook.schedulingservice.service.service.ServiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {
  @Mock
  private ScheduleRepository scheduleRepository;

  @InjectMocks
  private ScheduleServiceImpl scheduleService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createSchedule_ValidSchedule_ShouldReturnCreatedSchedule() {
    Schedule schedule = new Schedule();
    when(scheduleRepository.save(schedule)).thenReturn(schedule);

    Schedule result = scheduleService.createSchedule(schedule);

    assertEquals(schedule, result);
  }

  @Test
  void getScheduleById_ExistingId_ShouldReturnSchedule() {
    UUID scheduleId = UUID.randomUUID();
    Schedule schedule = new Schedule();
    when(scheduleRepository.findById(scheduleId)).thenReturn(Optional.of(schedule));

    Optional<Schedule> result = scheduleService.getScheduleById(scheduleId);

    assertEquals(Optional.of(schedule), result);
  }

  @Test
  void getSchedulesByUserId_ExistingUserId_ShouldReturnSchedules() {
    UUID userId = UUID.randomUUID();
    List<Schedule> schedules = Collections.singletonList(new Schedule());
    when(scheduleRepository.getSchedulesByUserCreatorId(userId)).thenReturn(schedules);

    List<Schedule> result = scheduleService.getSchedulesByUserId(userId);

    assertEquals(schedules, result);
  }

  @Test
  @Transactional
  void updateSchedule_ValidSchedule_ShouldReturnUpdatedSchedule() {
    Schedule schedule = new Schedule();
    schedule.setId(UUID.randomUUID());
    schedule.setTitle("Title");

    Schedule scheduleFromBase = new Schedule();
    scheduleFromBase.setId(schedule.getId());
    when(scheduleRepository.findById(schedule.getId())).thenReturn(Optional.of(scheduleFromBase));
    when(scheduleRepository.save(scheduleFromBase)).thenReturn(scheduleFromBase);

    Schedule result = scheduleService.updateSchedule(schedule);

    assertEquals(scheduleFromBase, result);
  }

  @Test
  void deleteScheduleById_ValidId_ShouldDeleteSchedule() {
    UUID scheduleId = UUID.randomUUID();

    scheduleService.deleteScheduleById(scheduleId);

    verify(scheduleRepository, times(1)).deleteById(scheduleId);
  }

  @Test
  void scheduleIsExists_ExistingId_ShouldReturnTrue() {
    UUID scheduleId = UUID.randomUUID();
    when(scheduleRepository.existsById(scheduleId)).thenReturn(true);

    boolean result = scheduleService.scheduleIsExists(scheduleId);

    assertEquals(true, result);
  }

  @Test
  void scheduleIsExists_NonExistingId_ShouldReturnFalse() {
    UUID scheduleId = UUID.randomUUID();
    when(scheduleRepository.existsById(scheduleId)).thenReturn(false);

    boolean result = scheduleService.scheduleIsExists(scheduleId);

    assertEquals(false, result);
  }
}

package com.easybook.schedulingservice.service;

import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.repository.ScheduleDateRepository;
import com.easybook.schedulingservice.service.schedule_date.ScheduleDateServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ScheduleDateServiceTest {
  @Mock
  private ScheduleDateRepository scheduleDateRepository;

  @InjectMocks
  private ScheduleDateServiceImpl scheduleDateService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void getScheduleDateById_ExistingId_ShouldReturnScheduleDate() {
    UUID scheduleDateId = UUID.randomUUID();
    ScheduleDate scheduleDate = new ScheduleDate();
    when(scheduleDateRepository.findById(scheduleDateId)).thenReturn(Optional.of(scheduleDate));

    Optional<ScheduleDate> result = scheduleDateService.getScheduleDateById(scheduleDateId);

    assertEquals(Optional.of(scheduleDate), result);
  }

  @Test
  void getAllScheduleDatesByScheduleId_ExistingScheduleId_ShouldReturnScheduleDates() {
    UUID scheduleId = UUID.randomUUID();
    List<ScheduleDate> scheduleDates = Collections.singletonList(new ScheduleDate());
    when(scheduleDateRepository.findScheduleDatesBySchedule_Id(scheduleId)).thenReturn(scheduleDates);

    List<ScheduleDate> result = scheduleDateService.getAllScheduleDatesByScheduleId(scheduleId);

    assertEquals(scheduleDates, result);
  }

  @Test
  void updateScheduleDate_ValidScheduleDate_ShouldReturnUpdatedScheduleDate() {
    ScheduleDate scheduleDate = new ScheduleDate();
    scheduleDate.setId(UUID.randomUUID());
    LocalDate newDate = LocalDate.now().plusDays(1);
    scheduleDate.setDate(newDate);

    ScheduleDate scheduleDateFromBase = new ScheduleDate();
    scheduleDateFromBase.setId(scheduleDate.getId());
    when(scheduleDateRepository.findById(scheduleDate.getId())).thenReturn(Optional.of(scheduleDateFromBase));
    when(scheduleDateRepository.save(scheduleDateFromBase)).thenReturn(scheduleDateFromBase);

    ScheduleDate result = scheduleDateService.updateScheduleDate(scheduleDate);

    assertEquals(scheduleDateFromBase, result);
  }

  @Test
  void deleteScheduleDateById_ValidId_ShouldDeleteScheduleDate() {
    UUID scheduleDateId = UUID.randomUUID();

    scheduleDateService.deleteScheduleDateById(scheduleDateId);

    verify(scheduleDateRepository, times(1)).deleteById(scheduleDateId);
  }
}

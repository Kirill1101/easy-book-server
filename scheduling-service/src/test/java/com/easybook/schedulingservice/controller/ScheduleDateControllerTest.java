package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.ScheduleDateCreateDto;
import com.easybook.schedulingservice.dto.regulardto.ScheduleDateDto;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.service.schedule_date.ScheduleDateService;
import com.easybook.schedulingservice.util.JwtUtil;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleDateControllerTest {

  @Mock
  private ScheduleDateService scheduleDateService;

  @Mock
  private ScheduleService scheduleService;

  @Mock
  private SchedulingMapper schedulingMapper;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private ScheduleDateController scheduleDateController;

  private static final String TOKEN = "Bearer testToken";
  private static final UUID USER_ID = UUID.randomUUID();
  private static final String USER_LOGIN = "testUser";

  @BeforeEach
  void setUp() {
    when(jwtUtil.validateTokenAndExtractData(anyString())).thenReturn(
        Map.of("id", USER_ID.toString(), "login", USER_LOGIN));
  }

  @Test
  void createScheduleDate_success() {
    ScheduleDateCreateDto createDto = new ScheduleDateCreateDto();
    createDto.setScheduleId(UUID.randomUUID());
    ScheduleDate scheduleDate = new ScheduleDate();
    Schedule schedule = new Schedule();
    schedule.setUserCreatorLogin(USER_LOGIN);
    scheduleDate.setSchedule(schedule);
    ScheduleDateDto dto = new ScheduleDateDto();

    when(scheduleService.getScheduleById(createDto.getScheduleId())).thenReturn(Optional.of(schedule));
    when(schedulingMapper.scheduleDateCreateDtoToScheduleDate(createDto)).thenReturn(scheduleDate);
    when(scheduleDateService.createScheduleDate(scheduleDate)).thenReturn(scheduleDate);
    when(schedulingMapper.scheduleDateToScheduleDateDto(scheduleDate)).thenReturn(dto);

    ScheduleDateDto result = scheduleDateController.createScheduleDate(createDto, TOKEN);

    assertNotNull(result);
    verify(scheduleDateService).createScheduleDate(scheduleDate);
    verify(schedulingMapper).scheduleDateToScheduleDateDto(scheduleDate);
  }

  @Test
  void createScheduleDate_scheduleNotFound() {
    ScheduleDateCreateDto createDto = new ScheduleDateCreateDto();
    createDto.setScheduleId(UUID.randomUUID());

    when(scheduleService.getScheduleById(createDto.getScheduleId())).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleDateController.createScheduleDate(createDto, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Расписания с id = " + createDto.getScheduleId() + " не существует", exception.getReason());
  }

  @Test
  void getScheduleDateById_notFound() {
    UUID id = UUID.randomUUID();

    when(scheduleDateService.getScheduleDateById(id)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleDateController.getScheduleDateById(id, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Даты в расписании с таким id не существует", exception.getReason());
  }

  @Test
  void deleteSlotById_notFound() {
    UUID id = UUID.randomUUID();

    when(scheduleDateService.getScheduleDateById(id)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleDateController.deleteSlotById(id, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Даты в расписании с таким id не существует", exception.getReason());
  }
}
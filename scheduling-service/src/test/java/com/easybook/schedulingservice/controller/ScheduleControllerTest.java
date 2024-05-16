package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.ScheduleCreateDto;
import com.easybook.schedulingservice.dto.regulardto.ScheduleDateDto;
import com.easybook.schedulingservice.dto.regulardto.ScheduleDto;
import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.organization.OrganizationService;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.service.schedule_date.ScheduleDateService;
import com.easybook.schedulingservice.service.service.ServiceService;
import com.easybook.schedulingservice.service.slot.SlotService;
import com.easybook.schedulingservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

  @Mock
  private SchedulingMapper schedulingMapper;

  @Mock
  private OrganizationService organizationService;

  @Mock
  private ScheduleService scheduleService;

  @Mock
  private ScheduleDateService scheduleDateService;

  @Mock
  private ServiceService serviceService;

  @Mock
  private SlotService slotService;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private ScheduleController scheduleController;

  private static final String TOKEN = "Bearer testToken";
  private static final UUID USER_ID = UUID.randomUUID();
  private static final String USER_LOGIN = "testUser";

  @BeforeEach
  void setUp() {
    when(jwtUtil.validateTokenAndExtractData(anyString())).thenReturn(Map.of("id", USER_ID.toString(), "login", USER_LOGIN));
  }

  @Test
  void createSchedule_success() {
    ScheduleCreateDto scheduleCreateDto = new ScheduleCreateDto();
    Schedule schedule = new Schedule();
    Schedule savedSchedule = new Schedule();
    ScheduleDto scheduleDto = new ScheduleDto();

    when(schedulingMapper.scheduleCreateDtoToSchedule(any(ScheduleCreateDto.class))).thenReturn(schedule);
    when(scheduleService.createSchedule(any(Schedule.class))).thenReturn(savedSchedule);
    when(schedulingMapper.scheduleToScheduleDto(any(Schedule.class))).thenReturn(scheduleDto);

    ScheduleDto result = scheduleController.createSchedule(scheduleCreateDto, TOKEN);

    assertNotNull(result);
    verify(scheduleService).createSchedule(schedule);
    verify(schedulingMapper).scheduleToScheduleDto(schedule);
  }

  @Test
  void getScheduleByUser_success() {
    List<Schedule> schedules = List.of(new Schedule(), new Schedule());
    List<ScheduleDto> scheduleDtos = List.of(new ScheduleDto(), new ScheduleDto());

    when(scheduleService.getSchedulesByUserId(USER_ID)).thenReturn(schedules);
    when(schedulingMapper.scheduleToScheduleDto(any(Schedule.class))).thenReturn(new ScheduleDto());

    List<ScheduleDto> result = scheduleController.getScheduleByUser(TOKEN);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(scheduleService).getSchedulesByUserId(USER_ID);
  }

  @Test
  void getScheduleById_success() {
    UUID scheduleId = UUID.randomUUID();
    Schedule schedule = new Schedule();
    ScheduleDto scheduleDto = new ScheduleDto();

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(schedule));
    when(schedulingMapper.scheduleToScheduleDto(schedule)).thenReturn(scheduleDto);

    ScheduleDto result = scheduleController.getScheduleById(scheduleId, TOKEN);

    assertNotNull(result);
    verify(scheduleService).getScheduleById(scheduleId);
    verify(schedulingMapper).scheduleToScheduleDto(schedule);
  }

  @Test
  void getScheduleById_notFound() {
    UUID scheduleId = UUID.randomUUID();

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleController.getScheduleById(scheduleId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Расписания с таким id не существует", exception.getReason());
  }

  @Test
  void deleteOrganization_success() {
    UUID scheduleId = UUID.randomUUID();
    Schedule existingSchedule = new Schedule();
    existingSchedule.setUserCreatorLogin(USER_LOGIN);

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(existingSchedule));

    scheduleController.deleteOrganization(scheduleId, TOKEN);

    verify(scheduleService).deleteScheduleById(scheduleId);
  }

  @Test
  void deleteOrganization_notFound() {
    UUID scheduleId = UUID.randomUUID();

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleController.deleteOrganization(scheduleId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Расписания с таким id не существует", exception.getReason());
  }

  @Test
  void deleteOrganization_forbidden() {
    UUID scheduleId = UUID.randomUUID();
    Schedule existingSchedule = new Schedule();
    existingSchedule.setUserCreatorLogin("anotherUser");

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(existingSchedule));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleController.deleteOrganization(scheduleId, TOKEN));
    assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    assertEquals("Нет прав доступа", exception.getReason());
  }

  @Test
  void getAllSlotsByScheduleId_success() {
    UUID scheduleId = UUID.randomUUID();
    List<ScheduleDate> scheduleDates = List.of(new ScheduleDate(), new ScheduleDate());
    List<ScheduleDateDto> scheduleDateDtos = List.of(new ScheduleDateDto(), new ScheduleDateDto());

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(new Schedule()));
    when(scheduleDateService.getAllScheduleDatesByScheduleId(scheduleId)).thenReturn(scheduleDates);
    when(schedulingMapper.scheduleDateToScheduleDateDto(any(ScheduleDate.class))).thenReturn(new ScheduleDateDto());

    List<ScheduleDateDto> result = scheduleController.getAllSlotsByScheduleId(scheduleId, TOKEN);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(scheduleDateService).getAllScheduleDatesByScheduleId(scheduleId);
  }

  @Test
  void getAllSlotsByScheduleId_notFound() {
    UUID scheduleId = UUID.randomUUID();

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleController.getAllSlotsByScheduleId(scheduleId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Расписания с таким id не существует", exception.getReason());
  }

  @Test
  void getFreeSlotsByScheduleId_success() {
    UUID scheduleId = UUID.randomUUID();
    List<ScheduleDate> scheduleDates = List.of(new ScheduleDate(), new ScheduleDate());

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(new Schedule()));
    when(scheduleDateService.getAllScheduleDatesByScheduleId(scheduleId)).thenReturn(scheduleDates);
    when(schedulingMapper.scheduleDateToScheduleDateDto(any(ScheduleDate.class))).thenReturn(new ScheduleDateDto());

    List<ScheduleDateDto> result = scheduleController.getFreeSlotsByScheduleId(scheduleId, TOKEN);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(scheduleDateService).getAllScheduleDatesByScheduleId(scheduleId);
  }

  @Test
  void getFreeSlotsByScheduleId_notFound() {
    UUID scheduleId = UUID.randomUUID();

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleController.getFreeSlotsByScheduleId(scheduleId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Расписания с таким id не существует", exception.getReason());
  }

  @Test
  void getAvailableSlotsByScheduleId_success() {
    UUID scheduleId = UUID.randomUUID();
    long durationInSeconds = 3600;
    List<ScheduleDate> scheduleDates = List.of(new ScheduleDate(), new ScheduleDate());

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.of(new Schedule()));
    when(scheduleDateService.getAllScheduleDatesByScheduleId(scheduleId)).thenReturn(scheduleDates);
    when(schedulingMapper.scheduleDateToScheduleDateDto(any(ScheduleDate.class))).thenReturn(new ScheduleDateDto());

    List<ScheduleDateDto> result = scheduleController.getAvailableSlotsByScheduleId(scheduleId, durationInSeconds, TOKEN);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(scheduleDateService).getAllScheduleDatesByScheduleId(scheduleId);
  }

  @Test
  void getAvailableSlotsByScheduleId_notFound() {
    UUID scheduleId = UUID.randomUUID();
    long durationInSeconds = 3600;

    when(scheduleService.getScheduleById(scheduleId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> scheduleController.getAvailableSlotsByScheduleId(scheduleId, durationInSeconds, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Расписания с таким id не существует", exception.getReason());
  }
}
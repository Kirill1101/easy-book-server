package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.AppointmentCreateDto;
import com.easybook.schedulingservice.dto.regulardto.AppointmentDto;
import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.appointment.AppointmentService;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentControllerTest {
  @Mock
  private AppointmentService appointmentService;

  @Mock
  private ScheduleService scheduleService;

  @Mock
  private SchedulingMapper schedulingMapper;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private AppointmentController appointmentController;

  private static final String TOKEN = "Bearer testToken";
  private static final String USER_LOGIN = "testUser";

  @BeforeEach
  void setUp() {
    when(jwtUtil.validateTokenAndExtractData(anyString())).thenReturn(Map.of("login", USER_LOGIN));
  }

  @Test
  void createAppointment_success() {
    AppointmentCreateDto appointmentCreateDto = new AppointmentCreateDto();
    appointmentCreateDto.setScheduleId(UUID.randomUUID());

    Schedule schedule = new Schedule();
    Appointment appointment = new Appointment();
    AppointmentDto appointmentDto = new AppointmentDto();

    when(scheduleService.getScheduleById(any(UUID.class))).thenReturn(Optional.of(schedule));
    when(schedulingMapper.appointmentCreateDtoToAppointment(any(AppointmentCreateDto.class)))
        .thenReturn(appointment);
    when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(appointment);
    when(schedulingMapper.appointmentToAppointmentDto(any(Appointment.class))).thenReturn(appointmentDto);

    AppointmentDto result = appointmentController.createAppointment(appointmentCreateDto, TOKEN);

    assertNotNull(result);
    verify(appointmentService).createAppointment(appointment);
    verify(schedulingMapper).appointmentToAppointmentDto(appointment);
  }

  @Test
  void createAppointment_scheduleNotFound() {
    AppointmentCreateDto appointmentCreateDto = new AppointmentCreateDto();
    appointmentCreateDto.setScheduleId(UUID.randomUUID());

    when(scheduleService.getScheduleById(any(UUID.class))).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> appointmentController.createAppointment(appointmentCreateDto, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Расписания с таким id не существует", exception.getReason());
  }

  @Test
  void getOrganizationByUser_success() {
    List<Appointment> appointments = List.of(new Appointment(), new Appointment());
    List<AppointmentDto> appointmentDtos = List.of(new AppointmentDto(), new AppointmentDto());

    when(appointmentService.getAllAppointmentsByUserLogin(USER_LOGIN)).thenReturn(appointments);
    when(schedulingMapper.appointmentToAppointmentDto(any(Appointment.class))).thenReturn(new AppointmentDto());

    List<AppointmentDto> result = appointmentController.getOrganizationByUser(TOKEN);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(appointmentService).getAllAppointmentsByUserLogin(USER_LOGIN);
  }

  @Test
  void getAppointmentById_success() {
    UUID appointmentId = UUID.randomUUID();
    Appointment appointment = new Appointment();
    AppointmentDto appointmentDto = new AppointmentDto();

    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.of(appointment));
    when(schedulingMapper.appointmentToAppointmentDto(appointment)).thenReturn(appointmentDto);

    AppointmentDto result = appointmentController.getAppointmentById(appointmentId, TOKEN);

    assertNotNull(result);
    verify(appointmentService).getAppointmentById(appointmentId);
    verify(schedulingMapper).appointmentToAppointmentDto(appointment);
  }

  @Test
  void getAppointmentById_notFound() {
    UUID appointmentId = UUID.randomUUID();

    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> appointmentController.getAppointmentById(appointmentId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Записи с таким id не существует", exception.getReason());
  }

  @Test
  void deleteAppointmentById_success() {
    UUID appointmentId = UUID.randomUUID();
    Appointment existingAppointment = new Appointment();
    existingAppointment.setUserLogin(USER_LOGIN);

    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.of(existingAppointment));

    appointmentController.deleteAppointmentById(appointmentId, TOKEN);

    verify(appointmentService).deleteAppointmentById(appointmentId);
  }

  @Test
  void deleteAppointmentById_notFound() {
    UUID appointmentId = UUID.randomUUID();

    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> appointmentController.deleteAppointmentById(appointmentId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Записи с таким id не существует", exception.getReason());
  }

  @Test
  void deleteAppointmentById_forbidden() {
    UUID appointmentId = UUID.randomUUID();
    Appointment existingAppointment = new Appointment();
    existingAppointment.setUserLogin("anotherUser");

    when(appointmentService.getAppointmentById(appointmentId)).thenReturn(Optional.of(existingAppointment));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> appointmentController.deleteAppointmentById(appointmentId, TOKEN));
    assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    assertEquals("Нет прав доступа", exception.getReason());
  }
}
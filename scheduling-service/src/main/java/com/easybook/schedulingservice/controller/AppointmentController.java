package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.AppointmentCreateDto;
import com.easybook.schedulingservice.dto.createdto.ServiceCreateDto;
import com.easybook.schedulingservice.dto.regulardto.AppointmentDto;
import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Service;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.repository.AppointmentRepository;
import com.easybook.schedulingservice.service.appointment.AppointmentService;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.util.JwtUtil;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {
  private final AppointmentService appointmentService;

  private final ScheduleService scheduleService;

  private final SchedulingMapper schedulingMapper;

  private final JwtUtil jwtUtil;

  private static final String APPOINTMENT_NOT_FOUND_MESSAGE = "Записи с таким id не существует";

  private static final String NO_ACCESS_RIGHTS_MESSAGE = "Нет прав доступа";

  @PostMapping
  public AppointmentDto createAppointment(
      @RequestBody AppointmentCreateDto appointmentCreateDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Optional<Schedule> schedule =
        scheduleService.getScheduleById(appointmentCreateDto.getScheduleId());
    if (schedule.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Расписания с таким id не существует");
    }

    Appointment appointment = schedulingMapper.appointmentCreateDtoToAppointment(appointmentCreateDto);
    appointment.setUserLogin(userLogin);
    appointment.setSchedule(schedule.get());
    appointment = appointmentService.createAppointment(appointment);

    return schedulingMapper.appointmentToAppointmentDto(appointment);
  }

  @GetMapping("/{id}")
  public AppointmentDto getAppointmentById(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Appointment appointment = appointmentService.getAppointmentById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, APPOINTMENT_NOT_FOUND_MESSAGE));

    return schedulingMapper.appointmentToAppointmentDto(appointment);
  }

  @PutMapping
  public AppointmentDto updateAppointment(
      @RequestBody AppointmentDto appointmentDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Appointment appointment = schedulingMapper.appointmentDtoToAppointment(appointmentDto);
    Optional<Appointment> appointmentFromBase =
        appointmentService.getAppointmentById(appointment.getId());

    if (appointmentFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, APPOINTMENT_NOT_FOUND_MESSAGE);
    } else if (!appointmentFromBase.get().getUserLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS_RIGHTS_MESSAGE);
    }

    appointment = appointmentService.updateAppointment(appointment);
    return schedulingMapper.appointmentToAppointmentDto(appointment);
  }

  @DeleteMapping("/{id}")
  public void deleteAppointmentById(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Optional<Appointment> appointmentFromBase = appointmentService.getAppointmentById(id);

    if (appointmentFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, APPOINTMENT_NOT_FOUND_MESSAGE);
    } else if (!appointmentFromBase.get().getUserLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS_RIGHTS_MESSAGE);
    }

    appointmentService.deleteAppointmentById(id);
  }
}

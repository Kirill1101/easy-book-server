package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.AppointmentCreateDto;
import com.easybook.schedulingservice.dto.createdto.ScheduleCreateDto;
import com.easybook.schedulingservice.dto.regulardto.AppointmentDto;
import com.easybook.schedulingservice.dto.regulardto.ScheduleDto;
import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.entity.Slot;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.organization.OrganizationService;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.service.schedule_date.ScheduleDateService;
import com.easybook.schedulingservice.service.service.ServiceService;
import com.easybook.schedulingservice.service.slot.SlotService;
import com.easybook.schedulingservice.util.JwtUtil;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

  private final SchedulingMapper schedulingMapper;

  private final OrganizationService organizationService;

  private final ScheduleService scheduleService;

  private final ScheduleDateService scheduleDateService;

  private final ServiceService serviceService;

  private final SlotService slotService;

  private final JwtUtil jwtUtil;

  private static final String SCHEDULE_NOT_FOUND_MESSAGE = "Расписания с таким id не существует";
  private static final String NO_ACCESS_RIGHTS_MESSAGE = "Нет прав доступа";

  @PostMapping
  public ScheduleDto createSchedule(
      @RequestBody ScheduleCreateDto scheduleCreateDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    Long userId = Long.valueOf(userInfo.get("id").toString());
    String userLogin = userInfo.get("login").toString();

    if (!organizationService.organizationIsExists(scheduleCreateDto.getOrganizationsId())) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Организации с id = " + scheduleCreateDto.getOrganizationsId() + " не существует");
    }

    Schedule schedule =
        schedulingMapper.scheduleCreateDtoToSchedule(scheduleCreateDto);
    schedule.setUserCreatorId(userId);
    schedule.setUserCreatorLogin(userLogin);
    Schedule savedSchedule = scheduleService.createSchedule(schedule);

    schedule.getServices().forEach(service -> {
      service.setUserCreatorLogin(userLogin);
      service.setSchedule(savedSchedule);
      serviceService.createService(service);
    });

    schedule.getAvailableDates().forEach(date -> {
      date.setSchedule(savedSchedule);
      scheduleDateService.createScheduleDate(date);
    });

    return schedulingMapper.scheduleToScheduleDto(schedule);
  }

  @GetMapping("/{id}")
  public ScheduleDto getScheduleById(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Schedule schedule = scheduleService.getScheduleById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            SCHEDULE_NOT_FOUND_MESSAGE));
    return schedulingMapper.scheduleToScheduleDto(schedule);
  }

  @PutMapping
  public ScheduleDto updateSchedule(
      @RequestBody ScheduleDto scheduleDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Schedule schedule =
        schedulingMapper.scheduleDtoToSchedule(scheduleDto);
    Optional<Schedule> scheduleFromBase =
        scheduleService.getScheduleById(schedule.getId());

    if (scheduleFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          SCHEDULE_NOT_FOUND_MESSAGE);
    } else if (!scheduleFromBase.get().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,
          NO_ACCESS_RIGHTS_MESSAGE);
    }
    schedule = scheduleService.updateSchedule(schedule);
    return schedulingMapper.scheduleToScheduleDto(schedule);
  }

  @DeleteMapping("/{id}")
  public void deleteOrganization(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());
    Optional<Schedule> scheduleFromBase =
        scheduleService.getScheduleById(id);

    if (scheduleFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          SCHEDULE_NOT_FOUND_MESSAGE);
    } else if (!scheduleFromBase.get().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,
          NO_ACCESS_RIGHTS_MESSAGE);
    }

    scheduleService.deleteScheduleById(id);
  }

  @GetMapping("/{scheduleId}/services")
  public List<ServiceDto> getAllServicesByScheduleId(
      @PathVariable Long scheduleId,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    return serviceService.getAllServicesByScheduleId(scheduleId).stream()
        .map(schedulingMapper::serviceToServiceDto)
        .toList();
  }

  @GetMapping("/{scheduleId}/slots")
  public List<ScheduleDate> getAllSlotsByScheduleId(
      @PathVariable Long scheduleId,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    if (scheduleService.getScheduleById(scheduleId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          SCHEDULE_NOT_FOUND_MESSAGE);
    }

    return scheduleDateService.getAllScheduleDatesByScheduleId(scheduleId);
  }

  @GetMapping("/{scheduleId}/free-slots")
  public List<ScheduleDate> getFreeSlotsByScheduleId(
      @PathVariable Long scheduleId,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    if (scheduleService.getScheduleById(scheduleId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          SCHEDULE_NOT_FOUND_MESSAGE);
    }

    List<ScheduleDate> scheduleDates = scheduleDateService.getAllScheduleDatesByScheduleId(
        scheduleId);
    scheduleDates.forEach(scheduleDate ->
        scheduleDate.setSlots(slotService.getFreeSlots(scheduleDate.getId())));
    return scheduleDates;
  }

  @GetMapping("/{scheduleId}/available-slots")
  public List<ScheduleDate> getFreeSlotsByScheduleId(
      @PathVariable Long scheduleId,
      @RequestParam Integer durationInSeconds,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    if (scheduleService.getScheduleById(scheduleId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          SCHEDULE_NOT_FOUND_MESSAGE);
    }

    List<ScheduleDate> scheduleDates = scheduleDateService.getAllScheduleDatesByScheduleId(
        scheduleId);
    scheduleDates.forEach(scheduleDate ->
        scheduleDate.setSlots(
            slotService.getAvailableSlotsForSpecifiedDuration(scheduleId,
                Duration.ofSeconds(durationInSeconds))
        )
    );
    return scheduleDates;
  }
}

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
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
    UUID userId = UUID.fromString(userInfo.get("id").toString());
    String userLogin = userInfo.get("login").toString();

    if (scheduleCreateDto.getOrganizationId() != null) {
      if (!organizationService.organizationIsExists(scheduleCreateDto.getOrganizationId())) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            "Организации с id = " + scheduleCreateDto.getOrganizationId() + " не существует");
      }
    }

    Schedule schedule =
        schedulingMapper.scheduleCreateDtoToSchedule(scheduleCreateDto);
    schedule.setUserCreatorId(userId);
    schedule.setUserCreatorLogin(userLogin);
    Schedule savedSchedule = scheduleService.createSchedule(schedule);

    if (schedule.getServices() != null) {
      schedule.getServices().forEach(service -> {
        service.setUserCreatorLogin(userLogin);
        service.setSchedule(savedSchedule);
        serviceService.createService(service);
      });
    }

    if (schedule.getAvailableDates() != null) {
      schedule.getAvailableDates().forEach(date -> {
        date.setSchedule(savedSchedule);
        scheduleDateService.createScheduleDate(date);
      });
    }

    return schedulingMapper.scheduleToScheduleDto(schedule);
  }

  @GetMapping
  public List<ScheduleDto> getScheduleByUser(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    UUID userId = UUID.fromString(userInfo.get("id").toString());

    return scheduleService.getSchedulesByUserId(userId).stream()
        .map(schedulingMapper::scheduleToScheduleDto)
        .toList();
  }

  @GetMapping("/{id}")
  public ScheduleDto getScheduleById(
      @PathVariable UUID id,
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
      @PathVariable UUID id,
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
      @PathVariable UUID scheduleId,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    return serviceService.getAllServicesByScheduleId(scheduleId).stream()
        .map(schedulingMapper::serviceToServiceDto)
        .toList();
  }

  @GetMapping("/{scheduleId}/slots")
  public List<ScheduleDateDto> getAllSlotsByScheduleId(
      @PathVariable UUID scheduleId,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    if (scheduleService.getScheduleById(scheduleId).isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          SCHEDULE_NOT_FOUND_MESSAGE);
    }

    return scheduleDateService.getAllScheduleDatesByScheduleId(scheduleId)
        .stream()
        .map(schedulingMapper::scheduleDateToScheduleDateDto)
        .toList();
  }

  @GetMapping("/{scheduleId}/free-slots")
  public ScheduleDto getFreeSlotsByScheduleId(
      @PathVariable UUID scheduleId,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Schedule schedule = scheduleService.getScheduleById(scheduleId).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, SCHEDULE_NOT_FOUND_MESSAGE));

    List<ScheduleDate> scheduleDates = scheduleDateService.getAllScheduleDatesByScheduleId(
        scheduleId);
    scheduleDates.forEach(scheduleDate ->
        scheduleDate.setSlots(slotService.getFreeSlots(scheduleDate.getId())));

    schedule.setAvailableDates(scheduleDates);

    return schedulingMapper.scheduleToScheduleDto(schedule);
  }

  @GetMapping("/{scheduleId}/available-slots")
  public ScheduleDto getAvailableSlotsByScheduleId(
      @PathVariable UUID scheduleId,
      @RequestParam(value = "durationInSeconds") Long durationInSeconds,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Schedule schedule = scheduleService.getScheduleById(scheduleId).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, SCHEDULE_NOT_FOUND_MESSAGE));

    List<ScheduleDate> scheduleDates = scheduleDateService.getAllScheduleDatesByScheduleId(
        scheduleId);
    scheduleDates.forEach(scheduleDate ->
        scheduleDate.setSlots(
            slotService.getAvailableSlotsForSpecifiedDuration(scheduleDate.getId(),
                Duration.ofSeconds(durationInSeconds))
        )
    );

    schedule.setAvailableDates(scheduleDates);

    return schedulingMapper.scheduleToScheduleDto(schedule);
  }
}

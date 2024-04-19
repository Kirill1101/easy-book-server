package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.ServiceCreateDto;
import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Service;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.service.service.ServiceService;
import com.easybook.schedulingservice.util.JwtUtil;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceController {

  private final ServiceService serviceService;

  private final ScheduleService scheduleService;

  private final SchedulingMapper schedulingMapper;

  private final JwtUtil jwtUtil;

  private static final String SERVICE_NOT_FOUND_MESSAGE = "Услуги с таким id не существует";

  private static final String NO_ACCESS_RIGHTS_MESSAGE = "Нет прав доступа";

  @PostMapping
  public ServiceDto createService(
      @RequestBody ServiceCreateDto serviceCreateDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Optional<Schedule> schedule =
        scheduleService.getScheduleById(serviceCreateDto.getScheduleId());
    if (schedule.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Расписания с id = " + serviceCreateDto.getScheduleId() + " не существует");
    }

    Service service = schedulingMapper.serviceCreateDtoToService(serviceCreateDto);
    service.setUserCreatorLogin(userLogin);
    service.setSchedule(schedule.get());

    service = serviceService.createService(service);
    return schedulingMapper.serviceToServiceDto(service);
  }

  @GetMapping("/{id}")
  public ServiceDto getServiceById(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Service service = serviceService.getServiceById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, SERVICE_NOT_FOUND_MESSAGE));

    return schedulingMapper.serviceToServiceDto(service);
  }

  @PutMapping
  public ServiceDto updateService(
      @RequestBody ServiceDto serviceDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Service service = schedulingMapper.serviceDtoToService(serviceDto);
    Optional<Service> serviceFromBase = serviceService.getServiceById(service.getId());

    if (serviceFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, SERVICE_NOT_FOUND_MESSAGE);
    } else if (!serviceFromBase.get().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS_RIGHTS_MESSAGE);
    }

    service = serviceService.updateService(service);
    return schedulingMapper.serviceToServiceDto(service);
  }

  @DeleteMapping("/{id}")
  public void deleteServiceById(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Optional<Service> serviceFromBase = serviceService.getServiceById(id);

    if (serviceFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, SERVICE_NOT_FOUND_MESSAGE);
    } else if (!serviceFromBase.get().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS_RIGHTS_MESSAGE);
    }

    serviceService.deleteServiceById(id);
  }
}

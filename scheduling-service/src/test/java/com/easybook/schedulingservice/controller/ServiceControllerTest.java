package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.ServiceCreateDto;
import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Service;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.service.service.ServiceService;
import com.easybook.schedulingservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

  @Mock
  private ServiceService serviceService;

  @Mock
  private ScheduleService scheduleService;

  @Mock
  private SchedulingMapper schedulingMapper;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private ServiceController serviceController;

  private static final String TOKEN = "Bearer testToken";
  private static final UUID USER_ID = UUID.randomUUID();
  private static final String USER_LOGIN = "testUser";

  @BeforeEach
  void setUp() {
    when(jwtUtil.validateTokenAndExtractData(TOKEN)).thenReturn(Map.of("login", USER_LOGIN));
  }

  @Test
  void createService_Success() {
    ServiceCreateDto createDto = new ServiceCreateDto();
    createDto.setScheduleId(UUID.randomUUID());
    Service service = new Service();
    ServiceDto dto = new ServiceDto();
    Schedule schedule = new Schedule();

    when(scheduleService.getScheduleById(createDto.getScheduleId())).thenReturn(Optional.of(schedule));
    when(schedulingMapper.serviceCreateDtoToService(createDto)).thenReturn(service);
    when(serviceService.createService(service)).thenReturn(service);
    when(schedulingMapper.serviceToServiceDto(service)).thenReturn(dto);

    ServiceDto result = serviceController.createService(createDto, TOKEN);

    assertNotNull(result);
    assertEquals(dto, result);
    verify(serviceService, times(1)).createService(service);
    verify(schedulingMapper, times(1)).serviceToServiceDto(service);
  }

  @Test
  void createService_ScheduleNotFound() {
    ServiceCreateDto createDto = new ServiceCreateDto();
    createDto.setScheduleId(UUID.randomUUID());

    when(scheduleService.getScheduleById(createDto.getScheduleId())).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> serviceController.createService(createDto, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void getServiceById_Success() {
    UUID id = UUID.randomUUID();
    Service service = new Service();
    ServiceDto dto = new ServiceDto();

    when(serviceService.getServiceById(id)).thenReturn(Optional.of(service));
    when(schedulingMapper.serviceToServiceDto(service)).thenReturn(dto);

    ServiceDto result = serviceController.getServiceById(id, TOKEN);

    assertNotNull(result);
    assertEquals(dto, result);
    verify(serviceService, times(1)).getServiceById(id);
    verify(schedulingMapper, times(1)).serviceToServiceDto(service);
  }

  @Test
  void getServiceById_NotFound() {
    UUID id = UUID.randomUUID();

    when(serviceService.getServiceById(id)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> serviceController.getServiceById(id, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }

  @Test
  void deleteServiceById_NotFound() {
    UUID id = UUID.randomUUID();

    when(serviceService.getServiceById(id)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> serviceController.deleteServiceById(id, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
  }
}








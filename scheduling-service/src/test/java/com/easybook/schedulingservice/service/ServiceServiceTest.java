package com.easybook.schedulingservice.service;

import com.easybook.schedulingservice.entity.Service;
import com.easybook.schedulingservice.repository.ServiceRepository;
import com.easybook.schedulingservice.service.service.ServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ServiceServiceTest {
  @Mock
  private ServiceRepository serviceRepository;

  @InjectMocks
  private ServiceServiceImpl serviceService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createService_ValidService_ShouldReturnCreatedService() {
    Service service = new Service();
    when(serviceRepository.save(service)).thenReturn(service);

    Service result = serviceService.createService(service);

    assertEquals(service, result);
  }

  @Test
  void getServiceById_ExistingId_ShouldReturnService() {
    UUID serviceId = UUID.randomUUID();
    Service service = new Service();
    when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

    Optional<Service> result = serviceService.getServiceById(serviceId);

    assertEquals(Optional.of(service), result);
  }

  @Test
  void getAllServicesByScheduleId_ExistingScheduleId_ShouldReturnServices() {
    UUID scheduleId = UUID.randomUUID();
    List<Service> services = Collections.singletonList(new Service());
    when(serviceRepository.findServicesByScheduleId(scheduleId)).thenReturn(services);

    List<Service> result = serviceService.getAllServicesByScheduleId(scheduleId);

    assertEquals(services, result);
  }

  @Test
  void updateService_ValidService_ShouldReturnUpdatedService() {
    Service service = new Service();
    service.setId(UUID.randomUUID());
    service.setTitle("Title");
    service.setDuration(60L);

    Service serviceFromBase = new Service();
    serviceFromBase.setId(service.getId());
    when(serviceRepository.findById(service.getId())).thenReturn(Optional.of(serviceFromBase));
    when(serviceRepository.save(serviceFromBase)).thenReturn(serviceFromBase);

    Service result = serviceService.updateService(service);

    assertEquals(serviceFromBase, result);
  }

  @Test
  void deleteServiceById_ValidId_ShouldDeleteService() {
    UUID serviceId = UUID.randomUUID();

    serviceService.deleteServiceById(serviceId);

    verify(serviceRepository, times(1)).deleteById(serviceId);
  }
}

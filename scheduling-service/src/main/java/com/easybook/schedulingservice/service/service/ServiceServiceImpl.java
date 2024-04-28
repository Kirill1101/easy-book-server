package com.easybook.schedulingservice.service.service;

import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.entity.Service;
import com.easybook.schedulingservice.repository.ServiceRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService{

  private final ServiceRepository serviceRepository;

  @Override
  public Service createService(Service service) {
    return serviceRepository.save(service);
  }

  @Override
  public Optional<Service> getServiceById(UUID id) {
    return serviceRepository.findById(id);
  }

  @Override
  public List<Service> getAllServicesByScheduleId(UUID scheduleId) {
    return serviceRepository.findServicesByScheduleId(scheduleId);
  }

  @Override
  public Service updateService(Service service) {
    Service serviceFromBase = getServiceById(service.getId()).orElseThrow();
    if (service.getTitle() != null) {
      serviceFromBase.setTitle(service.getTitle());
    }
    if (service.getDuration() != null) {
      serviceFromBase.setDuration(service.getDuration());
    }

    return serviceRepository.save(serviceFromBase);
  }

  @Override
  public void deleteServiceById(UUID id) {
    serviceRepository.deleteById(id);
  }
}

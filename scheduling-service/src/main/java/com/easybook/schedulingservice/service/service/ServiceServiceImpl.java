package com.easybook.schedulingservice.service.service;

import com.easybook.schedulingservice.entity.Service;
import com.easybook.schedulingservice.repository.ServiceRepository;
import java.util.List;
import java.util.Optional;
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
  public Optional<Service> getServiceById(Long id) {
    return serviceRepository.findById(id);
  }

  @Override
  public List<Service> getAllServicesByScheduleId(Long scheduleId) {
    return serviceRepository.findServicesByScheduleId(scheduleId);
  }

  @Override
  public Service updateService(Service service) {
    return serviceRepository.save(service);
  }

  @Override
  public void deleteServiceById(Long id) {
    serviceRepository.deleteById(id);
  }
}

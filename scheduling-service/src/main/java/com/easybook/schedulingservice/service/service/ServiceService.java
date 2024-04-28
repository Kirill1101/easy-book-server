package com.easybook.schedulingservice.service.service;

import com.easybook.schedulingservice.entity.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceService {

  Service createService(Service service);

  Optional<Service> getServiceById(UUID id);


  List<Service> getAllServicesByScheduleId(UUID scheduleId);


  Service updateService(Service service);


  void deleteServiceById(UUID id);
}

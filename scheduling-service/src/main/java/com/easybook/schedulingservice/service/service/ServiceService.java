package com.easybook.schedulingservice.service.service;

import com.easybook.schedulingservice.entity.Service;
import java.util.List;
import java.util.Optional;

public interface ServiceService {

  Service createService(Service service);

  Optional<Service> getServiceById(Long id);


  List<Service> getAllServicesByScheduleId(Long scheduleId);


  Service updateService(Service service);


  void deleteServiceById(Long id);


}

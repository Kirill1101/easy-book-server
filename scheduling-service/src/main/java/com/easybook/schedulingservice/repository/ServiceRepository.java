package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Service;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
  List<Service> findServicesByScheduleId(Long scheduleId);
}

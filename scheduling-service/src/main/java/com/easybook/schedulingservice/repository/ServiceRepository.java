package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Service;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, UUID> {
  List<Service> findServicesByScheduleId(UUID scheduleId);
}

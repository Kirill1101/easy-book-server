package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
  List<Organization> findOrganizationsByUserCreatorId(Long userCreatorId);
}

package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.entity.Schedule;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
  List<Organization> findOrganizationsByUserCreatorIdOrUserAdminLoginsContains(
      UUID userId, String userLogin);
}

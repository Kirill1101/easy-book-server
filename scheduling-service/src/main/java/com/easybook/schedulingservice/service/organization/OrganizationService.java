package com.easybook.schedulingservice.service.organization;

import com.easybook.schedulingservice.entity.Organization;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationService {
  Organization createOrganization(Organization organization);

  Organization updateOrganization(Organization organization);

  Optional<Organization> getOrganizationById(UUID id);

  List<Organization> getOrganizationsByUserId(UUID userId);

  void deleteOrganizationById(UUID id);

  boolean organizationIsExists(UUID id);
}

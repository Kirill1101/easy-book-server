package com.easybook.schedulingservice.service.organization;

import com.easybook.schedulingservice.entity.Organization;
import java.util.List;
import java.util.Optional;

public interface OrganizationService {
  Organization createOrganization(Organization organization);

  Organization updateOrganization(Organization organization);

  Optional<Organization> getOrganizationById(Long id);

  List<Organization> getOrganizationsByUserId(Long userId);

  void deleteOrganizationById(Long id);
}

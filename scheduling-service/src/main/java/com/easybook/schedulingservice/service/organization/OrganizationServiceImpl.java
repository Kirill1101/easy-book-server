package com.easybook.schedulingservice.service.organization;

import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.repository.OrganizationRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

  private static final String ORGANIZATION_NOT_FOUND_MESSAGE = "Организации с таким id не существует";

  private final OrganizationRepository organizationRepository;

  @Override
  public Organization createOrganization(Organization organization) {
    return organizationRepository.save(organization);
  }

  @Override
  public Organization updateOrganization(Organization organization) {
    return organizationRepository.save(organization);
  }

  @Override
  public Optional<Organization> getOrganizationById(UUID id) {
    return organizationRepository.findById(id);
  }

  @Override
  public List<Organization> getOrganizationsByUserIdAndLogin(UUID userId, String userLogin) {
    return organizationRepository
        .findOrganizationsByUserCreatorIdOrUserAdminLoginsContains(userId, userLogin);
  }

  @Override
  public void deleteOrganizationById(UUID id) {
    if (!organizationRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          ORGANIZATION_NOT_FOUND_MESSAGE);
    }
    organizationRepository.deleteById(id);
  }

  @Override
  public boolean organizationIsExists(UUID id) {
    return organizationRepository.existsById(id);
  }
}

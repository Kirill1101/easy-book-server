package com.easybook.schedulingservice.service;

import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.repository.OrganizationRepository;
import com.easybook.schedulingservice.service.organization.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

  @Mock
  private OrganizationRepository organizationRepository;

  @InjectMocks
  private OrganizationServiceImpl organizationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createOrganization_ValidOrganization_ShouldReturnCreatedOrganization() {
    Organization organization = new Organization();
    when(organizationRepository.save(organization)).thenReturn(organization);

    Organization result = organizationService.createOrganization(organization);

    assertEquals(organization, result);
  }

  @Test
  void updateOrganization_ValidOrganization_ShouldReturnUpdatedOrganization() {
    Organization organization = new Organization();
    when(organizationRepository.save(organization)).thenReturn(organization);

    Organization result = organizationService.updateOrganization(organization);

    assertEquals(organization, result);
  }

  @Test
  void getOrganizationById_ExistingId_ShouldReturnOrganization() {
    UUID organizationId = UUID.randomUUID();
    Organization organization = new Organization();
    when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));

    Optional<Organization> result = organizationService.getOrganizationById(organizationId);

    assertTrue(result.isPresent());
    assertEquals(organization, result.get());
  }

  @Test
  void getOrganizationById_NonExistingId_ShouldReturnEmptyOptional() {
    UUID organizationId = UUID.randomUUID();
    when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

    Optional<Organization> result = organizationService.getOrganizationById(organizationId);

    assertFalse(result.isPresent());
  }

  @Test
  void getOrganizationsByUserIdAndLogin_ValidInputs_ShouldReturnOrganizations() {
    UUID userId = UUID.randomUUID();
    String userLogin = "user";
    List<Organization> organizations = Collections.singletonList(new Organization());
    when(organizationRepository.findOrganizationsByUserCreatorIdOrUserAdminLoginsContains(userId, userLogin))
        .thenReturn(organizations);

    List<Organization> result = organizationService.getOrganizationsByUserIdAndLogin(userId, userLogin);

    assertEquals(organizations, result);
  }

  @Test
  void deleteOrganizationById_ExistingId_ShouldDeleteOrganization() {
    UUID organizationId = UUID.randomUUID();
    when(organizationRepository.existsById(organizationId)).thenReturn(true);

    organizationService.deleteOrganizationById(organizationId);

    verify(organizationRepository, times(1)).deleteById(organizationId);
  }

  @Test
  void deleteOrganizationById_NonExistingId_ShouldThrowException() {
    UUID organizationId = UUID.randomUUID();
    when(organizationRepository.existsById(organizationId)).thenReturn(false);

    assertThrows(ResponseStatusException.class, () ->
        organizationService.deleteOrganizationById(organizationId));
  }

  @Test
  void organizationIsExists_ExistingId_ShouldReturnTrue() {
    UUID organizationId = UUID.randomUUID();
    when(organizationRepository.existsById(organizationId)).thenReturn(true);

    boolean result = organizationService.organizationIsExists(organizationId);

    assertTrue(result);
  }

  @Test
  void organizationIsExists_NonExistingId_ShouldReturnFalse() {
    UUID organizationId = UUID.randomUUID();
    when(organizationRepository.existsById(organizationId)).thenReturn(false);

    boolean result = organizationService.organizationIsExists(organizationId);

    assertFalse(result);
  }
}
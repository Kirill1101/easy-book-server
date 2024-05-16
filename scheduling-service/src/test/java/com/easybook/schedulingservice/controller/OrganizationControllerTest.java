package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.OrganizationCreateDto;
import com.easybook.schedulingservice.dto.regulardto.OrganizationDto;
import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.organization.OrganizationService;
import com.easybook.schedulingservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationControllerTest {

  @Mock
  private SchedulingMapper schedulingMapper;

  @Mock
  private OrganizationService organizationService;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private OrganizationController organizationController;

  private static final String TOKEN = "Bearer testToken";
  private static final UUID USER_ID = UUID.randomUUID();
  private static final String USER_LOGIN = "testUser";

  @BeforeEach
  void setUp() {
    when(jwtUtil.validateTokenAndExtractData(anyString())).thenReturn(Map.of("id", USER_ID.toString(), "login", USER_LOGIN));
  }

  @Test
  void createOrganization_success() {
    OrganizationCreateDto organizationCreateDto = new OrganizationCreateDto();
    Organization organization = new Organization();
    OrganizationDto organizationDto = new OrganizationDto();

    when(schedulingMapper.organizationCreateDtoToOrganization(any(OrganizationCreateDto.class))).thenReturn(organization);
    when(organizationService.createOrganization(any(Organization.class))).thenReturn(organization);
    when(schedulingMapper.organizationToOrganizationDto(any(Organization.class))).thenReturn(organizationDto);

    OrganizationDto result = organizationController.createOrganization(organizationCreateDto, TOKEN);

    assertNotNull(result);
    verify(organizationService).createOrganization(organization);
    verify(schedulingMapper).organizationToOrganizationDto(organization);
  }

  @Test
  void getOrganizationByUser_success() {
    List<Organization> organizations = List.of(new Organization(), new Organization());
    List<OrganizationDto> organizationDtos = List.of(new OrganizationDto(), new OrganizationDto());

    when(organizationService.getOrganizationsByUserIdAndLogin(USER_ID, USER_LOGIN)).thenReturn(organizations);
    when(schedulingMapper.organizationToOrganizationDto(any(Organization.class))).thenReturn(new OrganizationDto());

    List<OrganizationDto> result = organizationController.getOrganizationByUser(TOKEN);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(organizationService).getOrganizationsByUserIdAndLogin(USER_ID, USER_LOGIN);
  }

  @Test
  void getOrganizationById_success() {
    UUID organizationId = UUID.randomUUID();
    Organization organization = new Organization();
    OrganizationDto organizationDto = new OrganizationDto();

    when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(organization));
    when(schedulingMapper.organizationToOrganizationDto(organization)).thenReturn(organizationDto);

    OrganizationDto result = organizationController.getOrganizationById(organizationId, TOKEN);

    assertNotNull(result);
    verify(organizationService).getOrganizationById(organizationId);
    verify(schedulingMapper).organizationToOrganizationDto(organization);
  }

  @Test
  void getOrganizationById_notFound() {
    UUID organizationId = UUID.randomUUID();

    when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> organizationController.getOrganizationById(organizationId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Организации с таким id не существует", exception.getReason());
  }

  @Test
  void deleteOrganization_success() {
    UUID organizationId = UUID.randomUUID();
    Organization existingOrganization = new Organization();
    existingOrganization.setUserCreatorLogin(USER_LOGIN);

    when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(existingOrganization));

    organizationController.deleteOrganization(organizationId, TOKEN);

    verify(organizationService).deleteOrganizationById(organizationId);
  }

  @Test
  void deleteOrganization_notFound() {
    UUID organizationId = UUID.randomUUID();

    when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.empty());

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> organizationController.deleteOrganization(organizationId, TOKEN));
    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertEquals("Организации с таким id не существует", exception.getReason());
  }

  @Test
  void deleteOrganization_forbidden() {
    UUID organizationId = UUID.randomUUID();
    Organization existingOrganization = new Organization();
    existingOrganization.setUserCreatorLogin("anotherUser");

    when(organizationService.getOrganizationById(organizationId)).thenReturn(Optional.of(existingOrganization));

    ResponseStatusException exception = assertThrows(ResponseStatusException.class,
        () -> organizationController.deleteOrganization(organizationId, TOKEN));
    assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    assertEquals("Нет прав доступа", exception.getReason());
  }
}
package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.OrganizationCreateDto;
import com.easybook.schedulingservice.dto.regulardto.OrganizationDto;
import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.organization.OrganizationService;
import com.easybook.schedulingservice.util.JwtUtil;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

  private final SchedulingMapper schedulingMapper;

  private final OrganizationService organizationService;

  private final JwtUtil jwtUtil;
  private static final String ORGANIZATION_NOT_FOUND_MESSAGE = "Организации с таким id не существует";
  private static final String NO_ACCESS_RIGHTS_MESSAGE = "Нет прав доступа";

  @PostMapping
  public OrganizationDto createOrganization(
      @RequestBody OrganizationCreateDto organizationCreateDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Organization organization =
        schedulingMapper.organizationCreateDtoToOrganization(organizationCreateDto);
    organization.setUserCreatorLogin(userLogin);
    organization.getSchedules().forEach(schedule -> schedule.setOrganization(organization));

    return schedulingMapper.organizationToOrganizationDto(
        organizationService.createOrganization(organization));
  }

  @GetMapping("/{id}")
  public OrganizationDto getOrganizationById(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Organization organization = organizationService.getOrganizationById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ORGANIZATION_NOT_FOUND_MESSAGE));
    return schedulingMapper.organizationToOrganizationDto(organization);
  }

  @PutMapping
  public OrganizationDto updateOrganization(
      @RequestBody OrganizationDto organizationDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Organization organization =
        schedulingMapper.organizationDtoToOrganization(organizationDto);
    Optional<Organization> organizationFromBase =
        organizationService.getOrganizationById(organization.getId());

    if (organizationFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          ORGANIZATION_NOT_FOUND_MESSAGE);
    } else if (!organizationFromBase.get().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,
          NO_ACCESS_RIGHTS_MESSAGE);
    }

    organization.getSchedules().forEach(schedule -> schedule.setOrganization(organization));

    return schedulingMapper.organizationToOrganizationDto(
        organizationService.updateOrganization(organization));
  }

  @DeleteMapping("/{id}")
  public void deleteOrganization(
      @PathVariable Long id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());
    Optional<Organization> organizationFromBase =
        organizationService.getOrganizationById(id);

    if (organizationFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          ORGANIZATION_NOT_FOUND_MESSAGE);
    } else if (!organizationFromBase.get().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN,
          NO_ACCESS_RIGHTS_MESSAGE);
    }

    organizationService.deleteOrganizationById(id);
  }
}
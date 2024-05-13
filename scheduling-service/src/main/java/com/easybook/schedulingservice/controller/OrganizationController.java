package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.OrganizationCreateDto;
import com.easybook.schedulingservice.dto.regulardto.OrganizationDto;
import com.easybook.schedulingservice.dto.regulardto.ScheduleDto;
import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.organization.OrganizationService;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.util.JwtUtil;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

  private final ScheduleService scheduleService;

  private final JwtUtil jwtUtil;
  private static final String ORGANIZATION_NOT_FOUND_MESSAGE = "Организации с таким id не существует";
  private static final String NO_ACCESS_RIGHTS_MESSAGE = "Нет прав доступа";

  @PostMapping
  public OrganizationDto createOrganization(
      @RequestBody OrganizationCreateDto organizationCreateDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    UUID userId = UUID.fromString(userInfo.get("id").toString());
    String userLogin = userInfo.get("login").toString();

    Organization organization =
        schedulingMapper.organizationCreateDtoToOrganization(organizationCreateDto);
    organization.setUserCreatorId(userId);
    organization.setUserCreatorLogin(userLogin);

    return schedulingMapper.organizationToOrganizationDto(
        organizationService.createOrganization(organization));
  }

  @GetMapping
  public List<OrganizationDto> getOrganizationByUser(
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    UUID userId = UUID.fromString(userInfo.get("id").toString());
    String userLogin = String.valueOf(userInfo.get("login").toString());

    return organizationService.getOrganizationsByUserIdAndLogin(userId, userLogin).stream()
        .map(schedulingMapper::organizationToOrganizationDto)
        .toList();
  }

  @GetMapping("/{id}")
  public OrganizationDto getOrganizationById(
      @PathVariable UUID id,
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
    } else if (organizationDto.getUserCreatorLogin() !=null &&
        !organizationFromBase.get().getUserCreatorLogin()
        .equals(organizationDto.getUserCreatorLogin())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "Пользователь " + organizationDto.getUserCreatorLogin() +
              " не является создателем организации с id " + organization.getId());
    }

    organization.getSchedules().forEach(schedule -> schedule.setOrganizationId(organization.getId()));
    organization.setUserCreatorId(organizationFromBase.get().getUserCreatorId());
    organization.setUserCreatorLogin(organizationFromBase.get().getUserCreatorLogin());
    return schedulingMapper.organizationToOrganizationDto(
        organizationService.updateOrganization(organization));
  }

  @DeleteMapping("/{id}")
  public void deleteOrganization(
      @PathVariable UUID id,
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

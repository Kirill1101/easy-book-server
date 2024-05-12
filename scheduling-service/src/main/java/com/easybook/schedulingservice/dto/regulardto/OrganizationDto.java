package com.easybook.schedulingservice.dto.regulardto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class OrganizationDto {
  @NotNull
  private UUID id;

  private String userCreatorLogin;

  private String title;

  private List<ScheduleDto> schedules;

  private List<String> userAdminLogins;
}

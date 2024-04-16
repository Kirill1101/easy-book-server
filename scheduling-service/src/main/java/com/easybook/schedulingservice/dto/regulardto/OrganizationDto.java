package com.easybook.schedulingservice.dto.regulardto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class OrganizationDto {
  @NotNull
  private Long id;

  private String userCreatorLogin;

  private Long title;

  private List<ScheduleDto> schedules;
}

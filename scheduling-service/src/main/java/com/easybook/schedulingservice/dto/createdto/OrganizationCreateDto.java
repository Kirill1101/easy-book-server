package com.easybook.schedulingservice.dto.createdto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class OrganizationCreateDto {
  @NotNull
  private Long title;

  private List<ScheduleCreateDto> schedules;
}

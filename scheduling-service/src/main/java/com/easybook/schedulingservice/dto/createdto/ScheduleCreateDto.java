package com.easybook.schedulingservice.dto.createdto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ScheduleCreateDto {
  @NotNull
  private Long title;

  private List<SlotCreateDto> slots;

  private List<ServiceCreateDto> service;
}

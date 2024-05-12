package com.easybook.schedulingservice.dto.createdto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class ServiceCreateDto {
  @NotNull
  private String title;

  @NotNull
  private Long duration;

  private Long price;

  private UUID scheduleId;
}

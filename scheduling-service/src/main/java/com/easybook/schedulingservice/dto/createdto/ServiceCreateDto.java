package com.easybook.schedulingservice.dto.createdto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;

@Data
public class ServiceCreateDto {
  @NotNull
  private String title;

  @NotNull
  private Instant duration;
}

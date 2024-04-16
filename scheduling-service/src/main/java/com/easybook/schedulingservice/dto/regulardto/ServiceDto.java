package com.easybook.schedulingservice.dto.regulardto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Data;

@Data
public class ServiceDto {
  @NotNull
  private Long id;

  private String userCreatorLogin;

  private String title;

  private Instant duration;
}

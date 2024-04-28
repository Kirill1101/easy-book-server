package com.easybook.schedulingservice.dto.regulardto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.Data;

@Data
public class ServiceDto {
  @NotNull
  private UUID id;

  private String userCreatorLogin;

  private String title;

  private Long duration;
}

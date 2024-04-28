package com.easybook.schedulingservice.dto.createdto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Data;

@Data
public class SlotCreateDto {
  @NotNull
  private LocalDate date;

  @NotNull
  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime startTime;

  @NotNull
  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime endTime;

  private UUID scheduleDateId;
}

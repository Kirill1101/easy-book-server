package com.easybook.schedulingservice.dto.regulardto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class SlotDto {
  @NotNull
  private Long id;

  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime startTime;

  @Schema(type = "String", pattern = "12:00:00")
  private LocalTime endTime;

  private Long appointmentId;
}

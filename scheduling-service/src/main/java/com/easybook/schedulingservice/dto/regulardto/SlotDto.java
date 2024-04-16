package com.easybook.schedulingservice.dto.regulardto;

import com.easybook.schedulingservice.entity.Service;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class SlotDto {
  @NotNull
  private Long id;

  private LocalDate localDate;

  private LocalTime startTime;

  private LocalTime endTime;

  private Long appointmentId;
}

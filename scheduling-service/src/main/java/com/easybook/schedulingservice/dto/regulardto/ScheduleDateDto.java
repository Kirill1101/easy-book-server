package com.easybook.schedulingservice.dto.regulardto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class ScheduleDateDto {
  @NotNull
  private Long id;

  @NotNull
  private LocalDate date;

  @NotNull
  private List<SlotDto> slots;
}

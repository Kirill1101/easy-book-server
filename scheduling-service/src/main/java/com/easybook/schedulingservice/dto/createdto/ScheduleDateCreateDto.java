package com.easybook.schedulingservice.dto.createdto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class ScheduleDateCreateDto {
  @NotNull
  private LocalDate date;

  @NotNull
  private List<SlotCreateDto> slots;

  @NotNull
  private UUID scheduleId;
}

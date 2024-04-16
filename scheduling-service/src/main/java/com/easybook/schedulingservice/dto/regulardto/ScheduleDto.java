package com.easybook.schedulingservice.dto.regulardto;

import com.easybook.schedulingservice.dto.createdto.ServiceCreateDto;
import com.easybook.schedulingservice.entity.Slot;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ScheduleDto {
  @NotNull
  private Long id;

  private String userCreatorLogin;

  private Long title;

  private List<Slot> slots;

  private List<ServiceDto> service;
}

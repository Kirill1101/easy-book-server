package com.easybook.schedulingservice.dto.createdto;

import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import com.easybook.schedulingservice.entity.Schedule;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SlotCreateDto {
  @NotNull
  private Schedule schedule;

  private Long userIdWhoBookedSlot;

  private ServiceDto service;

  private Long scheduleId;
}

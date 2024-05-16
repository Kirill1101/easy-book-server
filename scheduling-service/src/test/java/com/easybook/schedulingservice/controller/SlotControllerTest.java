package com.easybook.schedulingservice.controller;

import com.easybook.schedulingservice.dto.createdto.SlotCreateDto;
import com.easybook.schedulingservice.dto.regulardto.SlotDto;
import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.entity.Slot;
import com.easybook.schedulingservice.mapper.SchedulingMapper;
import com.easybook.schedulingservice.service.schedule.ScheduleService;
import com.easybook.schedulingservice.service.schedule_date.ScheduleDateService;
import com.easybook.schedulingservice.service.slot.SlotService;
import com.easybook.schedulingservice.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotControllerTest {

  @Mock
  private SlotService slotService;

  @Mock
  private ScheduleDateService scheduleDateService;

  @Mock
  private SchedulingMapper schedulingMapper;

  @Mock
  private JwtUtil jwtUtil;

  @InjectMocks
  private SlotController slotController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createSlot_InvalidScheduleDateId_ShouldThrowException() {
    SlotCreateDto slotCreateDto = new SlotCreateDto();
    slotCreateDto.setScheduleDateId(UUID.randomUUID());

    when(scheduleDateService.getScheduleDateById(slotCreateDto.getScheduleDateId())).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () ->
        slotController.createSlot(slotCreateDto, "token"));
  }

  @Test
  void getSlotById_ExistingSlotId_ShouldReturnSlotDto() {
    UUID slotId = UUID.randomUUID();
    Slot slot = new Slot();
    when(slotService.getSlotById(slotId)).thenReturn(Optional.of(slot));

    SlotDto slotDto = new SlotDto();
    when(schedulingMapper.slotToSlotDto(slot)).thenReturn(slotDto);

    SlotDto result = slotController.getSlotById(slotId, "token");

    assertNotNull(result);
  }

  @Test
  void getSlotById_NonExistingSlotId_ShouldThrowException() {
    UUID slotId = UUID.randomUUID();
    when(slotService.getSlotById(slotId)).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () ->
        slotController.getSlotById(slotId, "token"));
  }

  @Test
  void deleteSlotById_NonExistingSlotId_ShouldThrowException() {
    UUID slotId = UUID.randomUUID();

    when(jwtUtil.validateTokenAndExtractData("token")).thenReturn(Map.of("login", "userLogin"));

    when(slotService.getSlotById(slotId)).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () ->
        slotController.deleteSlotById(slotId, "token"));
  }
}
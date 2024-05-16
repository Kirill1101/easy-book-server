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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/slot")
@RequiredArgsConstructor
public class SlotController {
  private final SlotService slotService;

  private final ScheduleService scheduleService;

  private final ScheduleDateService scheduleDateService;

  private final SchedulingMapper schedulingMapper;

  private final JwtUtil jwtUtil;

  private static final String SLOT_NOT_FOUND_MESSAGE = "Слота с таким id не существует";

  private static final String NO_ACCESS_RIGHTS_MESSAGE = "Нет прав доступа";

  @PostMapping
  public SlotDto createSlot(
      @RequestBody SlotCreateDto slotCreateDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Optional<ScheduleDate> scheduleDate =
        scheduleDateService.getScheduleDateById(slotCreateDto.getScheduleDateId());
    if (scheduleDate.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          "Даты в расписании с id = " + slotCreateDto.getScheduleDateId() + " не существует");
    }

    Slot slot = schedulingMapper.slotCreateDtoToSlot(slotCreateDto);
    slot.setScheduleDate(scheduleDate.get());
    slot = slotService.createSlot(slot);

    return schedulingMapper.slotToSlotDto(slot);
  }

  @GetMapping("/{id}")
  public SlotDto getSlotById(
      @PathVariable UUID id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    jwtUtil.validateTokenAndExtractData(token);

    Slot slot = slotService.getSlotById(id).orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, SLOT_NOT_FOUND_MESSAGE));

    return schedulingMapper.slotToSlotDto(slot);
  }

  @PutMapping
  public SlotDto updateSlot(
      @RequestBody SlotDto slotDto,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Slot slot = schedulingMapper.slotDtoToSlot(slotDto);
    Optional<Slot> slotFromBase = slotService.getSlotById(slot.getId());

    if (slotFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, SLOT_NOT_FOUND_MESSAGE);
    } else if (!slotFromBase.get().getScheduleDate().getSchedule().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS_RIGHTS_MESSAGE);
    }

    slot = slotService.updateSlot(slot);
    return schedulingMapper.slotToSlotDto(slot);
  }

  @DeleteMapping("/{id}")
  public void deleteSlotById(
      @PathVariable UUID id,
      @RequestHeader(value = HttpHeaders.AUTHORIZATION) String token) {
    Map<String, Object> userInfo = jwtUtil.validateTokenAndExtractData(token);
    String userLogin = String.valueOf(userInfo.get("login").toString());

    Optional<Slot> slotFromBase = slotService.getSlotById(id);

    if (slotFromBase.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, SLOT_NOT_FOUND_MESSAGE);
    } else if (!slotFromBase.get().getScheduleDate().getSchedule().getUserCreatorLogin().equals(userLogin)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, NO_ACCESS_RIGHTS_MESSAGE);
    }

    slotService.deleteSlotById(id);
  }
}

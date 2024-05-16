package com.easybook.schedulingservice.service;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.ScheduleDate;
import com.easybook.schedulingservice.entity.Slot;
import com.easybook.schedulingservice.repository.ScheduleDateRepository;
import com.easybook.schedulingservice.repository.SlotRepository;
import com.easybook.schedulingservice.service.slot.SlotServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotServiceTest {
  @Mock
  private SlotRepository slotRepository;

  @Mock
  private ScheduleDateRepository scheduleDateRepository;

  @InjectMocks
  private SlotServiceImpl slotService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createSlot_ValidSlot_ShouldReturnCreatedSlot() {
    Slot slot = new Slot();
    when(slotRepository.save(slot)).thenReturn(slot);

    Slot result = slotService.createSlot(slot);

    assertEquals(slot, result);
  }

  @Test
  void getSlotById_ExistingId_ShouldReturnSlot() {
    UUID slotId = UUID.randomUUID();
    Slot slot = new Slot();
    when(slotRepository.findById(slotId)).thenReturn(Optional.of(slot));

    Optional<Slot> result = slotService.getSlotById(slotId);

    assertEquals(Optional.of(slot), result);
  }

  @Test
  void getAllSlotsByScheduleDateId_ExistingScheduleDateId_ShouldReturnSlots() {
    UUID scheduleDateId = UUID.randomUUID();
    List<Slot> slots = Collections.singletonList(new Slot());
    when(slotRepository.findSlotsByScheduleDate_Id(scheduleDateId)).thenReturn(slots);

    List<Slot> result = slotService.getAllSlotsByScheduleDateId(scheduleDateId);

    assertEquals(slots, result);
  }

  @Test
  void getFreeSlots_ValidScheduleDateId_ShouldReturnFreeSlots() {
    UUID scheduleDateId = UUID.randomUUID();
    List<Slot> freeSlots = Collections.singletonList(new Slot());
    when(slotRepository.findSlotsByScheduleDate_IdAndAppointmentIdIsNull(scheduleDateId)).thenReturn(freeSlots);

    List<Slot> result = slotService.getFreeSlots(scheduleDateId);

    assertEquals(freeSlots, result);
  }

  @Test
  void getOccupiedSlots_ValidScheduleDateId_ShouldReturnOccupiedSlots() {
    UUID scheduleDateId = UUID.randomUUID();
    List<Slot> occupiedSlots = Collections.singletonList(new Slot());
    when(slotRepository.findSlotsByScheduleDate_IdAndAppointmentIdIsNotNull(scheduleDateId)).thenReturn(occupiedSlots);

    List<Slot> result = slotService.getOccupiedSlots(scheduleDateId);

    assertEquals(occupiedSlots, result);
  }

  @Test
  void updateSlot_ValidSlot_ShouldReturnUpdatedSlot() {
    Slot slot = new Slot();
    slot.setId(UUID.randomUUID());
    slot.setStartTime(LocalTime.of(9, 0));
    slot.setEndTime(LocalTime.of(10, 0));

    Slot slotFromBase = new Slot();
    slotFromBase.setId(slot.getId());
    when(slotRepository.findById(slot.getId())).thenReturn(Optional.of(slotFromBase));
    when(slotRepository.save(slotFromBase)).thenReturn(slotFromBase);

    Slot result = slotService.updateSlot(slot);

    assertEquals(slotFromBase, result);
  }

  @Test
  void updateSlot_NonExistingSlot_ShouldThrowResponseStatusException() {
    Slot slot = new Slot();
    slot.setId(UUID.randomUUID());

    when(slotRepository.findById(slot.getId())).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () -> slotService.updateSlot(slot));
  }

  @Test
  void deleteSlotById_ValidId_ShouldDeleteSlot() {
    UUID slotId = UUID.randomUUID();

    slotService.deleteSlotById(slotId);

    verify(slotRepository, times(1)).deleteById(slotId);
  }

  @Test
  void setSlotsAsOccupiedByAppointment_NoScheduleDateFound_ShouldThrowResponseStatusException() {
    UUID appointmentId = UUID.randomUUID();
    Appointment appointment = new Appointment();
    appointment.setId(appointmentId);
    appointment.setDate(LocalDate.now());
    Schedule schedule = new Schedule();
    schedule.setId(UUID.randomUUID());
    appointment.setSchedule(schedule);

    when(scheduleDateRepository.findScheduleDateBySchedule_IdAndDate(schedule.getId(), LocalDate.now()))
        .thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () -> slotService.setSlotsAsOccupiedByAppointment(appointment));
  }

  @Test
  void getAvailableSlotsForSpecifiedDuration_ValidScheduleDateIdAndDuration_ShouldReturnAvailableSlots() {
    UUID scheduleDateId = UUID.randomUUID();
    Duration duration = Duration.ofMinutes(30);
    List<Slot> freeSlots = new ArrayList<>();
    Slot slot1 = new Slot();
    slot1.setStartTime(LocalTime.of(9, 0));
    slot1.setEndTime(LocalTime.of(9, 30));
    Slot slot2 = new Slot();
    slot2.setStartTime(LocalTime.of(9, 30));
    slot2.setEndTime(LocalTime.of(10, 0));
    freeSlots.add(slot1);
    freeSlots.add(slot2);

    when(slotRepository.findSlotsByScheduleDate_IdAndAppointmentIdIsNull(scheduleDateId)).thenReturn(freeSlots);

    List<Slot> result = slotService.getAvailableSlotsForSpecifiedDuration(scheduleDateId, duration);

    assertEquals(2, result.size());
    assertEquals(slot1, result.get(0));
    assertEquals(slot2, result.get(1));
  }

  @Test
  void getAvailableSlotsForSpecifiedDuration_NoAvailableSlots_ShouldReturnEmptyList() {
    UUID scheduleDateId = UUID.randomUUID();
    Duration duration = Duration.ofMinutes(30);

    when(slotRepository.findSlotsByScheduleDate_IdAndAppointmentIdIsNull(scheduleDateId)).thenReturn(Collections.emptyList());

    List<Slot> result = slotService.getAvailableSlotsForSpecifiedDuration(scheduleDateId, duration);

    assertTrue(result.isEmpty());
  }

  @Test
  void getSlotsByAppointmentId_ValidAppointmentId_ShouldReturnSlots() {
    UUID appointmentId = UUID.randomUUID();
    List<Slot> slots = Collections.singletonList(new Slot());

    when(slotRepository.getSlotsByAppointmentId(appointmentId)).thenReturn(slots);

    List<Slot> result = slotService.getSlotsByAppointmentId(appointmentId);

    assertEquals(slots, result);
  }
}
